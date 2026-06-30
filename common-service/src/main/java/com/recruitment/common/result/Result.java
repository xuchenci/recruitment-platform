package com.recruitment.common.result;

import java.io.Serializable;

/**
 * 统一返回结果类
 */
public class Result<T> implements Serializable {
    
    private Integer code;
    
    private String message;
    
    private T data;
    
    private Long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public void setCode(int code) {
        this.code = code;
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
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public static <T> Result<T> success() {
        return success(null);
    }
    
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }
    
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    
    public static <T> Result<T> badRequest(String message) {
        return error(400, message);
    }
    
    public static <T> Result<T> unauthorized(String message) {
        return error(401, message);
    }
    
    public static <T> Result<T> forbidden(String message) {
        return error(403, message);
    }
    
    public static <T> Result<T> notFound(String message) {
        return error(404, message);
    }
}