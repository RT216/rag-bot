package org.uestc.weglas.core.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.uestc.weglas.core.model.Conversation;
import org.uestc.weglas.core.model.ConversationChatDetail;
import reactor.core.publisher.Flux;

/**
 * @author yingxian.cyx
 * @date Created in 2024/10/12
 */
@Service
public class ChatServiceImpl implements ChatService {

    private final Logger logger = LogManager.getLogger(ChatServiceImpl.class);

    /**
     * @param conversation 历史会话
     * @param currentChat
     * @return
     */
    @Override
    public ConversationChatDetail chat(Conversation conversation, ConversationChatDetail currentChat) {
        return null;
    }

    /**
     * 流式读取
     *
     * @param conversation 会话
     * @param userChat     用户输入chat
     * @return 流式返回文本
     */
    @Override
    public Flux<String> streamChat(Conversation conversation, ConversationChatDetail userChat) {
        return null;
    }

}
