package org.uestc.weglas.core.converter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.springframework.util.CollectionUtils;
import org.uestc.weglas.base.entity.ConversationEntity;
import org.uestc.weglas.core.model.Conversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author yingxian.cyx
 * @date Created in 2024/9/20
 */
public class ConversationConverter {


    public static List<Conversation> convert(List<ConversationEntity> conversationEntities) {
        if (CollectionUtils.isEmpty(conversationEntities)) {
            return Collections.emptyList();
        }

        List<Conversation> conversations = new ArrayList<>();
        for (ConversationEntity conversationEntity : conversationEntities) {
            Conversation conversation = convert(conversationEntity);
            conversations.add(conversation);
        }
        return conversations;
    }

    public static Conversation convert(ConversationEntity conversationEntity) {
        Conversation conversation = new Conversation();
        conversation.setId(conversationEntity.getId());
        conversation.setTitle(conversationEntity.getTitle());
        conversation.setLlmModel(conversationEntity.getLlmModel());
        conversation.setExt(JSON.parseObject(conversationEntity.getExt(), new TypeReference<Map<String, String>>() {
        }));
        conversation.setGmtCreate(conversationEntity.getGmtCreate());
        conversation.setGmtModified(conversationEntity.getGmtModified());
        return conversation;
    }

    public static ConversationEntity convert(Conversation conversation) {
        ConversationEntity entity = new ConversationEntity();
        entity.setId(conversation.getId());
        entity.setTitle(conversation.getTitle());
        entity.setLlmModel(conversation.getLlmModel());
        entity.setExt(JSON.toJSONString(conversation.getExt()));
        entity.setGmtCreate(conversation.getGmtCreate());
        entity.setGmtModified(conversation.getGmtModified());
        return entity;
    }
}
