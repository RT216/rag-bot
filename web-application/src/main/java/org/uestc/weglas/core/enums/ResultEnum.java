package org.uestc.weglas.core.enums;

/**
 * @author yingxian.cyx
 * @date Created in 2024/10/14
 */
public enum ResultEnum {

    SUCCESS("SUCCESS", "接口调用成功"),
    PARAMETER_ILLEGAL("PARAMETER_ILLEGAL", "参数校验失败"),
    INVOKE_FAIL("INVOKE_FAIL", "接口调用失败"),
    ;

    private String code;
    private String message;

    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
