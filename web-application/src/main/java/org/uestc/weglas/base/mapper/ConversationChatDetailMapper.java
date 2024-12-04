package org.uestc.weglas.base.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.uestc.weglas.base.entity.ConversationChatDetailEntity;
import org.uestc.weglas.base.entity.ConversationEntity;
import org.uestc.weglas.biz.dto.ConversationQuery;

import java.util.List;

/**
 * <p>
 * Conversation Chat Detail Mapper
 * </p>
 *
 * @author yingxian.cyx
 * @date Created in 2024-06-08 10:54
 */
@Mapper
@Component
public interface ConversationChatDetailMapper {

    /**
     * 查询所有聊天
     *
     * @return 监控列表
     */
    @Select("SELECT * FROM ai_conversation_chat_detail WHERE conversation_id = #{conversationId} order by gmt_create")
    List<ConversationChatDetailEntity> selectByConversationId(@Param("conversationId") Integer conversationId);

    /**
     * 保存
     *
     * @param entity 会话
     * @return 成功 - {@code 1} 失败 - {@code 0}
     */
    int insert(ConversationChatDetailEntity entity);

    /**
     * @param entity
     */
    void updateById(ConversationChatDetailEntity entity);

    /**
     * 删除
     *
     * @param id 主键id
     * @return 成功 - {@code 1} 失败 - {@code 0}
     */
    int deleteById(@Param("id") Integer id);

    @Select("<script>" +
            "SELECT * FROM ai_conversation " +
            "WHERE 1=1 " +
            "<if test='query.id != null and query.id != \"\"'> " +
            "AND id = #{query.id} </if>" +
            "<if test='query.title != null and query.title != \"\"'> " +
            "AND title = #{query.title} </if>" +
            "order by gmt_modified DESC" +
            "</script>")
    List<ConversationEntity> queryByConditions(@Param("query") ConversationQuery query);

}
