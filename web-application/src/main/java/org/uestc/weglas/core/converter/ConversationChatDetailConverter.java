package org.uestc.weglas.core.converter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.uestc.weglas.base.entity.ConversationChatDetailEntity;
import org.uestc.weglas.core.model.ConversationChatDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yingxian.cyx
 * @date Created in 2024/9/23
 */
public class ConversationChatDetailConverter {

    public static List<ConversationChatDetail> convert(List<ConversationChatDetailEntity> chatList) {

        List<ConversationChatDetail> chats = new ArrayList<>();
        for (ConversationChatDetailEntity chat : chatList) {
            chats.add(convert(chat));
        }
        return chats;
    }

    public static ConversationChatDetail convert(ConversationChatDetailEntity entity) {
        ConversationChatDetail chat = new ConversationChatDetail();
        chat.setId(entity.getId());
        chat.setConversationId(entity.getConversationId());
        chat.setType(entity.getType());
        chat.setRole(entity.getRole());
        chat.setContent(entity.getContent());

        chat.setExt(JSON.parseObject(entity.getExt(), new TypeReference<Map<String, String>>() {
        }));
        chat.setGmtCreate(entity.getGmtCreate());
        chat.setGmtModified(entity.getGmtModified());
        return chat;
    }

    public static ConversationChatDetailEntity convert(ConversationChatDetail chat) {
        ConversationChatDetailEntity entity = new ConversationChatDetailEntity();
        entity.setId(chat.getId());
        entity.setConversationId(chat.getConversationId());
        entity.setType(chat.getType());
        entity.setRole(chat.getRole());
        entity.setContent(chat.getContent());

        entity.setExt(JSON.toJSONString(chat.getExt()));
        entity.setGmtCreate(chat.getGmtCreate());
        entity.setGmtModified(chat.getGmtModified());
        return entity;
    }
}
