package org.uestc.weglas.biz.dto;
import lombok.Data;
/**
 * RAG流式聊天响应块
 */
@Data
public class RagChatStreamChunk {
    /**
     * 当前块的内容
     */
    private String content;

    /**
     * 是否是最后一块
     */
    private boolean isLast;

    /**
     * 完整的响应（仅在最后一块中包含）
     */
    private String fullResponse;
}