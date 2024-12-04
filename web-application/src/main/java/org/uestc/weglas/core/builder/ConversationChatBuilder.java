package org.uestc.weglas.core.builder;

import org.uestc.weglas.core.model.Conversation;
import org.uestc.weglas.core.model.ConversationChatDetail;

/**
 * @author yingxian.cyx
 * @date Created in 2024/10/12
 */
public class ConversationChatBuilder {

    public static ConversationChatDetail buildAssistantChat(Conversation conversation, String content) {
        ConversationChatDetail chat = new ConversationChatDetail();
        chat.setConversationId(conversation.getId());
        chat.setType("TEXT");
        chat.setContent(content);
        chat.setRole("assistant");

        return chat;
    }


    public static ConversationChatDetail buildDefaultChat(Conversation conversation) {
        ConversationChatDetail chat = new ConversationChatDetail();
        chat.setConversationId(conversation.getId());
        chat.setContent(conversation.getTitle());
        chat.setRole("user");
        chat.setType("TEXT");
        return chat;
    }

}
