package org.uestc.weglas.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uestc.weglas.base.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 聊天记录
 * @author yingxian.cyx
 * @date Created in 2024/10/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationChatDetail extends ToString {

    private Integer id;

    private Integer conversationId;

    /**
     * 会话角色：SYSTEM/USER/ASSISTANT...
     */
    private String role;

    /**
     * 类型：TEXT/IMAGE/VIDEO
     */
    private String type;
    private String content;

    private Map<String,String> ext = new HashMap<>();


    private Date gmtCreate;

    private Date gmtModified;
}