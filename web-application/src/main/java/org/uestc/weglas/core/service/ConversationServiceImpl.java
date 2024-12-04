package org.uestc.weglas.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uestc.weglas.base.entity.ConversationChatDetailEntity;
import org.uestc.weglas.base.entity.ConversationEntity;
import org.uestc.weglas.base.mapper.ConversationChatDetailMapper;
import org.uestc.weglas.base.mapper.ConversationMapper;
import org.uestc.weglas.core.builder.ConversationChatBuilder;
import org.uestc.weglas.core.converter.ConversationChatDetailConverter;
import org.uestc.weglas.core.converter.ConversationConverter;
import org.uestc.weglas.core.model.Conversation;
import org.uestc.weglas.core.model.ConversationChatDetail;

import java.util.List;

/**
 * @author yingxian.cyx
 * @date Created in 2024/10/11
 */
@Service
@Slf4j
public class ConversationServiceImpl implements ConversationService {

    /**
     * （仓储层）数据表映射mapper
     */
    @Autowired
    private ConversationMapper conversationMapper;

    /**
     * （仓储层）数据表映射mapper
     */
    @Autowired
    private ConversationChatDetailMapper conversationChatDetailMapper;

    /**
     * 新建会话，包括新建一条conversation记录+chat_detail记录
     * @param conversation 会话
     */
    @Override
    @Transactional
    public void add(Conversation conversation) {
        ConversationEntity entity = ConversationConverter.convert(conversation);
        conversationMapper.insert(entity);
        // 设置db返回的主键id
        conversation.setId(entity.getId());

        ConversationChatDetail userChat = ConversationChatBuilder.buildDefaultChat(conversation);
        ConversationChatDetailEntity chatEntity = ConversationChatDetailConverter.convert(userChat);
        conversationChatDetailMapper.insert(chatEntity);
        userChat.setId(chatEntity.getId());
    }

    /**
     * 单条会话查询
     * @param conversationId key值
     * @return
     */
    @Override
    public Conversation queryById(Integer conversationId) {
        ConversationEntity conversationEntity = conversationMapper.selectById(conversationId);
        Conversation basicConversation = ConversationConverter.convert(conversationEntity);

        List<ConversationChatDetailEntity> chatDetailEntities = conversationChatDetailMapper.selectByConversationId(conversationId);
        List<ConversationChatDetail> chatList = ConversationChatDetailConverter.convert(chatDetailEntities);

        basicConversation.setChatList(chatList);

        return basicConversation;
    }

    /**
     * TODO 会话记录列表查询
     * @return
     */
    @Override
    public List<Conversation> queryAll() {
        return null;
    }

    /**
     * TODO 新建一条chat_detail记录
     * @param chat 会话id
     */
    @Override
    @Transactional
    public void addChat(ConversationChatDetail chat) {
    }

    /**
     * TODO 删除一条chat_detail记录
     * @param chatId
     */
    @Override
    public void removeChat(Integer chatId) {
    }
}
