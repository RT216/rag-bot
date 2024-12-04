package org.uestc.weglas.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uestc.weglas.biz.dto.RagChatRequest;
import org.uestc.weglas.biz.dto.RagChatResponse;
import org.uestc.weglas.core.model.Conversation;
import org.uestc.weglas.core.model.ConversationChatDetail;
import org.uestc.weglas.util.exception.AssertUtil;
import org.uestc.weglas.util.exception.ManagerBizException;
import org.uestc.weglas.core.enums.ResultEnum;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 聊天服务实现类
 *
 * @author yingxian.cyx
 * @date Created in 2024/10/12
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private RagServiceClient ragServiceClient;
    
    @Autowired
    private ConversationService conversationService;

    /**
     * 普通对话
     *
     * @param conversation 历史会话
     * @param currentChat 当前用户输入
     * @return AI回复的消息
     */
    @Override
    public ConversationChatDetail chat(Conversation conversation, ConversationChatDetail userChat) {
        try {
            // 1. 参数校验
            AssertUtil.notNull(conversation);
            AssertUtil.notNull(userChat);
            AssertUtil.notNull(conversation.getId());
            
            // 2. 保存用户消息
            userChat.setRole("user");
            userChat.setConversationId(conversation.getId());
            conversationService.addChat(userChat);
            
            // 3. 获取历史消息
            List<String> historyMessages = conversation.getChatList().stream()
                .map(ConversationChatDetail::getContent)
                .collect(Collectors.toList());
            
            // 4. 调用RAG服务获取回复
            String aiResponse = ragServiceClient.chat(
                userChat.getContent(), 
                historyMessages
            );
            
            // 5. 创建并保存AI回复
            ConversationChatDetail aiChat = new ConversationChatDetail();
            aiChat.setConversationId(conversation.getId());
            aiChat.setContent(aiResponse);
            aiChat.setRole("assistant");
            aiChat.setType("TEXT");
            conversationService.addChat(aiChat);
            
            return aiChat;
            
        } catch (Exception e) {
            log.error("处理对话失败", e);
            throw new ManagerBizException(ResultEnum.INVOKE_FAIL);
        }
    }

    /**
     * 流式对话
     *
     * @param conversation 会话
     * @param userChat 用户输入chat
     * @return 流式返回文本
     */
    @Override
    public Flux<String> streamChat(Conversation conversation, ConversationChatDetail userChat) {
        try {
            // 参数校验
            AssertUtil.notNull(conversation);
            AssertUtil.notNull(userChat);
            AssertUtil.notNull(conversation.getId());
            
            // 获取历史消息
            List<String> historyMessages = conversation.getChatList().stream()
                .map(ConversationChatDetail::getContent)
                .collect(Collectors.toList());
            
            // 保存用户消息
            userChat.setRole("user");
            userChat.setConversationId(conversation.getId());
            conversationService.addChat(userChat);
            
            // 调用RAG服务获取流式回复
            return ragServiceClient.streamChat(
                userChat.getContent(), 
                historyMessages
            ).map(chunk -> {
                // 如果是最后一个chunk，保存完整的AI回复
                if (chunk.isLast()) {
                    ConversationChatDetail aiChat = new ConversationChatDetail();
                    aiChat.setConversationId(conversation.getId());
                    aiChat.setContent(chunk.getFullResponse());
                    aiChat.setRole("assistant");
                    conversationService.addChat(aiChat);
                }
                return chunk.getContent();
            });
            
        } catch (Exception e) {
            log.error("处理流式对话失败", e);
            return Flux.error(new ManagerBizException(ResultEnum.INVOKE_FAIL));
        }
    }
}