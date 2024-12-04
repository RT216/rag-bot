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
import org.uestc.weglas.util.exception.AssertUtil;
import org.uestc.weglas.util.exception.ManagerBizException;
import org.uestc.weglas.core.enums.ResultEnum;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

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

    @Autowired
    private ChatService chatService;  // 注入ChatService

    /**
     * 新建会话，包括新建一条conversation记录+chat_detail记录
     * @param conversation 会话
     */
    @Override
    @Transactional
    public void add(Conversation conversation) {
        // 1. 保存会话基本信息
        ConversationEntity entity = ConversationConverter.convert(conversation);
        conversationMapper.insert(entity);
        // 设置db返回的主键id
        conversation.setId(entity.getId());

        // 2. 创建第一条用户消息
        ConversationChatDetail userChat = new ConversationChatDetail();
        userChat.setContent(conversation.getTitle());  // 使用title作为第一条消息
        userChat.setConversationId(conversation.getId());
        userChat.setType("TEXT");
        userChat.setRole("user");

        // 3. 调用chatService处理对话
        try {
            chatService.chat(conversation, userChat);
        } catch (Exception e) {
            log.error("处理首条消息失败", e);
            throw new ManagerBizException(ResultEnum.INVOKE_FAIL);
        }
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
     * Lecture 4 会话记录列表查询
     * @return 所有会话列表
     */
    @Override
    public List<Conversation> queryAll() {
        // 1. 获取所有会话基本信息，已经按gmt_modified DESC排序
        List<ConversationEntity> conversationEntities = conversationMapper.selectAll();
        List<Conversation> conversations = new ArrayList<>();
        
        // 2. 为每个会话获取对话记录
        for (ConversationEntity entity : conversationEntities) {
            if (Objects.isNull(entity)) {
                continue;
            }
            
            Conversation conversation = ConversationConverter.convert(entity);
            
            // 获取该会话的所有对话，已经按gmt_create排序
            List<ConversationChatDetailEntity> chatDetails = 
                conversationChatDetailMapper.selectByConversationId(entity.getId());
            
            if (!chatDetails.isEmpty()) {
                conversation.setChatList(
                    ConversationChatDetailConverter.convert(chatDetails)
                );
            }
            
            conversations.add(conversation);
        }
        
        return conversations;
    }

    /**
     * Lecture 4 新建一条chat_detail记录
     * @param chat 对话详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addChat(ConversationChatDetail chat) {
        // 1. 参数校验
        AssertUtil.notNull(chat);
        AssertUtil.notNull(chat.getConversationId());
        
        try {
            // 2. 转换并保存实体
            ConversationChatDetailEntity chatEntity = 
                ConversationChatDetailConverter.convert(chat);
            int result = conversationChatDetailMapper.insert(chatEntity);
            
            if (result <= 0) {
                throw new ManagerBizException(ResultEnum.INVOKE_FAIL);
            }
            
            // 3. 设置返回的ID
            chat.setId(chatEntity.getId());
            
            // 4. 更新会话的最后更新时间
            conversationMapper.updateGmtModifiedById(chat.getConversationId());
            
        } catch (ManagerBizException e) {
            log.error("添加对话详情失败", e);
            throw e;
        } catch (Exception e) {
            log.error("添加对话详情失败", e);
            throw new ManagerBizException(ResultEnum.INVOKE_FAIL);
        }
    }

    /**
     * Lecture 4 删除一条chat_detail记录
     * @param chatId 对话详情ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeChat(Integer chatId) {
        // 1. 参数校验
        AssertUtil.notNull(chatId);
        
        try {
            // 2. 删除对话详情
            int result = conversationChatDetailMapper.deleteById(chatId);
            
            if (result <= 0) {
                throw new ManagerBizException(ResultEnum.INVOKE_FAIL);
            }
            
        } catch (ManagerBizException e) {
            log.error("删除对话详情失败", e);
            throw e;
        } catch (Exception e) {
            log.error("删除对话详情失败", e);
            throw new ManagerBizException(ResultEnum.INVOKE_FAIL);
        }
    }
}
