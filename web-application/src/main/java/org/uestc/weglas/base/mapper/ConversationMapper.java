package org.uestc.weglas.base.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.uestc.weglas.base.entity.ConversationEntity;
import org.uestc.weglas.biz.dto.ConversationQuery;

import java.util.List;

/**
 * <p>
 * Conversation Mapper
 * </p>
 *
 * @author yingxian.cyx
 * @date Created in 2024-06-08 10:54
 */
@Mapper
@Component
public interface ConversationMapper {

    /**
     * 查询所有学生
     *
     * @return 监控列表
     */
    @Select("SELECT * FROM ai_conversation order by gmt_modified DESC")
    List<ConversationEntity> selectAll();

    /**
     * 根据id查询
     *
     * @param id 主键id
     * @return 当前id的会话，不存在则是 {@code null}
     */
    @Select("SELECT * FROM ai_conversation WHERE id = #{id}")
    ConversationEntity selectById(@Param("id") Integer id);

    /**
     * 保存
     *
     * @param entity 会话
     * @return 成功 - {@code 1} 失败 - {@code 0}
     */
    int insert(ConversationEntity entity);

    /**
     * @param entity
     */
    void updateById(ConversationEntity entity);

    /**
     * 根据id查询
     *
     * @param id 主键id
     * @return 当前id的会话，不存在则是 {@code null}
     */
    void updateGmtModifiedById(@Param("id") Integer id);

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
