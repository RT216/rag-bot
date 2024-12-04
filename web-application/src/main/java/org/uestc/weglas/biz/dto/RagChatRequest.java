package org.uestc.weglas.biz.dto;

import lombok.Data;
import java.util.List;

@Data
public class RagChatRequest {
    private String message;
    private List<String> historyMessages;
}