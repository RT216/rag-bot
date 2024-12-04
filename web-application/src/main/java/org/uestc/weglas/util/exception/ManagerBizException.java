package org.uestc.weglas.util.exception;

import org.uestc.weglas.core.enums.ResultEnum;

/**
 * 定义通用业务异常
 *
 * @author yingxian.cyx
 * @date Created in 2024/6/21
 */
public class ManagerBizException extends RuntimeException {

    public ManagerBizException() {
        super();
    }

    public ManagerBizException(String message) {
        super(message);
    }

    public ManagerBizException(ResultEnum resultCode) {
        super(resultCode.getCode());
    }

    public ManagerBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerBizException(Throwable cause) {
        super(cause);
    }
}
