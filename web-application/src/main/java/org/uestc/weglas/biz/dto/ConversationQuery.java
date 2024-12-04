package org.uestc.weglas.biz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uestc.weglas.base.ToString;

/**
 * @author yingxian.cyx
 * @date Created in 2024/9/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationQuery extends ToString {

    private String id;

    private String title;

    private int pageNum = 1;

    private int pageSize = 10;
}
