package org.uestc.weglas.util.exception;

import org.apache.commons.lang.StringUtils;
import org.uestc.weglas.core.enums.ResultEnum;

/**
 * 断言工具类
 *
 * @author yingxian.cyx
 * @date Created in 2024/6/21
 */
public class AssertUtil {

    // isTrue 方法
    public static void isTrue(boolean condition) {
        if (!condition) {
            throw new ManagerBizException(ResultEnum.PARAMETER_ILLEGAL);
        }
    }

    public static void notBlank(String message) {
        if (StringUtils.isBlank(message)) {
            throw new ManagerBizException(ResultEnum.PARAMETER_ILLEGAL);
        }
    }

    public static void notNull(Object object) {
        if (object == null) {
            throw new ManagerBizException(ResultEnum.PARAMETER_ILLEGAL);
        }
    }
}