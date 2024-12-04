package org.uestc.weglas.biz.dto;

import lombok.Data;

@Data
public class RagChatResponse {
    private String response;
    private String status;
    private String error;
}
