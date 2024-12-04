package org.uestc.weglas.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.uestc.weglas.core.enums.ResultEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 */
public class BaseResult<T> {
    private boolean success;
    private String message;
    private T data;
    private List<T> values = new ArrayList<>();

    public BaseResult() {
    }

    public BaseResult(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public BaseResult(boolean success, String message, List<T> values) {
        this.success = success;
        this.message = message;
        this.values = values;
    }

    public static <T> BaseResult<T> success(T data) {
        return new BaseResult<>(true, "Operation successful", data);
    }

    public static <T> BaseResult<T> success(List<T> values) {
        return new BaseResult<>(true, "Operation successful", values);
    }

    public static <T> BaseResult<T> fail(ResultEnum resultCode) {
        return new BaseResult<>(false, resultCode.getCode(), null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }
}
