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
        private boolean isLast;
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
        
        return webClient.post()
            .uri("/chat/stream")
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody(request)
            .retrieve()
            .bodyToFlux(StreamChunk.class)
            .onErrorMap(e -> {
                log.error("调用RAG流式服务失败", e);
                return new ManagerBizException(ResultEnum.INVOKE_FAIL);
            });
    }
}
