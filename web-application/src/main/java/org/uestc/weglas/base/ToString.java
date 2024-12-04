package org.uestc.weglas.base;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author yingxian.cyx
 * @date Created in 2024/9/19
 */
public abstract class ToString implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
