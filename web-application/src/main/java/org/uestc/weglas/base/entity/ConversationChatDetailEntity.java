package org.uestc.weglas.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uestc.weglas.base.ToString;

import java.util.Date;

/**
 * <p>
 * 聊天详情表
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationChatDetailEntity extends ToString {


  private Integer id;

  /**
   * 会话id
   */
  private Integer conversationId;

  /**
   * 会话角色：system/user/assistant...
   */
  private String role;

  /**
   * 类型：TEXT/IMAGE/VIDEO
   */
  private String type;
  private String content;

  private String ext;

  private Date gmtCreate;

  private Date gmtModified;
}
