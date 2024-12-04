package org.uestc.weglas.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uestc.weglas.base.ToString;

import java.util.*;

/**
 * @author yingxian.cyx
 * @date Created in 2024/10/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Conversation extends ToString {


    private Integer id;

    private String title;

    private String llmModel;

    private Map<String, String> ext = new HashMap<>();


    private List<ConversationChatDetail> chatList = new ArrayList<>();

    private Date gmtCreate;

    private Date gmtModified;


}
