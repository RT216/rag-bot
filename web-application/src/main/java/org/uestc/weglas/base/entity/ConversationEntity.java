package org.uestc.weglas.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uestc.weglas.base.ToString;

import java.util.Date;

/**
 * <p>
 * 会话信息表
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationEntity extends ToString {

    private static final long serialVersionUID = 2892248514883451461L;

    /**
     * 会话id
     */
    private Integer id;

    /**
     * 会话名
     */
    private String title;
    private String llmModel;

    private String ext;

    private Date gmtCreate;

    private Date gmtModified;
}
