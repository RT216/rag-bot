package org.uestc.weglas.util.log;

import org.apache.logging.log4j.Logger;

/**
 * 日志工具类
 *
 * @author yingxian.cyx
 * @date Created in 2024/6/21
 */
public class LogUtil {

    /**
     * @param logger
     * @param message
     */
    public static void debug(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * @param logger
     * @param var
     */
    public static void info(Logger logger, Object... var) {
        if (logger.isInfoEnabled()) {
            logger.info(var);
        }
    }

    /**
     * @param logger
     * @param var
     */
    public static void warn(Logger logger, Object... var) {
        if (logger.isWarnEnabled()) {
            logger.warn(var);
        }
    }

    /**
     * @param logger
     * @param t
     * @param message
     */
    public static void warn(Logger logger, Throwable t, String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, t);
        }
    }

    /**
     * @param logger
     * @param message
     */
    public static void error(Logger logger, String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    /**
     * @param logger
     * @param t
     * @param message
     */
    public static void error(Logger logger, Throwable t, String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message, t);
        }
    }
}
