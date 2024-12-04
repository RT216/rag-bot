package org.uestc.weglas.core.service;

import org.uestc.weglas.core.model.Conversation;
import org.uestc.weglas.core.model.ConversationChatDetail;
import reactor.core.publisher.Flux;

/**
 * @author yingxian.cyx
 * @date Created in 2024/10/12
 */
public interface ChatService {

    /**
     * 聊天
     *
     * @param conversation 历史会话
     * @return 操作结果
     */
    ConversationChatDetail chat(Conversation conversation, ConversationChatDetail currentChat);

    /**
     * 支持流式输出
     *
     * @param conversation
     * @param chat
     * @return
     */
    Flux<String> streamChat(Conversation conversation, ConversationChatDetail chat);
}
