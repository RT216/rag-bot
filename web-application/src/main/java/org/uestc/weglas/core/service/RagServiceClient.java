package org.uestc.weglas.core.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.uestc.weglas.biz.dto.RagChatRequest;
import org.uestc.weglas.biz.dto.RagChatResponse;
import org.uestc.weglas.util.exception.ManagerBizException;
import org.uestc.weglas.core.enums.ResultEnum;
import reactor.core.publisher.Flux;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class RagServiceClient {
    
    @Value("${rag.service.url}")
    private String ragServiceUrl;
    
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    
    public RagServiceClient(RestTemplate restTemplate, WebClient.Builder webClientBuilder) {
        this.restTemplate = restTemplate;
        this.webClient = webClientBuilder.baseUrl(ragServiceUrl).build();
    }
    
    @Data
    public static class StreamChunk {
        private String content;
        
        @JsonProperty("isLast")  // 使用这个注解来映射 JSON 中的 "isLast" 字段
        private boolean last;     // Java 中使用 "last" 作为字段名
        
        private String fullResponse;
    }
    
    /**
     * 普通对话
     */
    public String chat(String message, List<String> historyMessages) {
        try {
            RagChatRequest request = new RagChatRequest();
            request.setMessage(message);
            request.setHistoryMessages(historyMessages);
            log.info("Sending normal request: {}", request);  // 添加日志
            
            RagChatResponse response = restTemplate.postForObject(
                ragServiceUrl + "/chat",
                request,
                RagChatResponse.class
            );
            
            if (response == null || !"success".equals(response.getStatus())) {
                throw new ManagerBizException(ResultEnum.INVOKE_FAIL);
            }
            
            return response.getResponse();
        } catch (Exception e) {
            log.error("调用RAG服务失败", e);
            throw new ManagerBizException(ResultEnum.INVOKE_FAIL);
        }
    }
    
    /**
     * 流式对话
     */
    public Flux<StreamChunk> streamChat(String message, List<String> historyMessages) {
        RagChatRequest request = new RagChatRequest();
        request.setMessage(message);
        request.setHistoryMessages(historyMessages);
        
        log.info("Sending stream request: {}", request);  // 添加日志
        
        return webClient.post()
            .uri(ragServiceUrl + "/chat/stream")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)  // 添加Accept头
            .syncBody(request)
            .retrieve()
            .bodyToFlux(String.class)  // 先作为String接收
            .map(this::parseChunk)     // 然后解析JSON
            // .bodyToFlux(StreamChunk.class)
            .doOnSubscribe(subscription -> log.info("Stream request started"))
            .doOnNext(chunk -> log.debug("Received chunk: {}", chunk))
            .doOnError(error -> log.error("Stream error", error))
            .doOnComplete(() -> log.info("Stream request completed"));
    }
    private StreamChunk parseChunk(String jsonStr) {
        try {
            // 使用Jackson或其他JSON库解析
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonStr, StreamChunk.class);
        } catch (Exception e) {
            log.error("Error parsing chunk: {}", jsonStr, e);
            throw new RuntimeException("Error parsing stream chunk", e);
        }
    }
}
