package org.uestc.weglas.core.service;

import org.uestc.weglas.core.model.Conversation;
import org.uestc.weglas.core.model.ConversationChatDetail;

import java.util.List;

/**
 * @author yingxian.cyx
 * @date Created in 2024/10/11
 */
public interface ConversationService {

    /**
     * 新增会话
     *
     * @param conversation 会话
     * @return 操作结果
     */
    void add(Conversation conversation);


    /**
     * 获取会话详情
     *
     * @param conversationId key值
     * @return 返回结果
     */
    Conversation queryById(Integer conversationId);


    /**
     * 获取列表
     *
     * @return 返回结果
     */
    List<Conversation> queryAll();

    /**
     * 新增聊天
     *
     * @param chat 会话id
     * @return 操作结果
     */
    void addChat( ConversationChatDetail chat);

    /**
     * 删除聊天
     *
     * @param chatId
     * @return 操作结果
     */
    void removeChat(Integer chatId);
}
