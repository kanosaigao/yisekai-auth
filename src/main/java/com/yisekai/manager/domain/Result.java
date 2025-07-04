package com.yisekai.manager.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;


@SuppressWarnings("all")
public class Result<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 成功 */
    public static final Integer SUCCESS = ResultCode.SUCCESS.getCode();

    /** 失败 */
    public static final Integer FAIL = ResultCode.FAILURE.getCode();


    /**
     * 响应编码
     */
    private Integer code=SUCCESS;
    /**
     * 提示消息
     */
    private String message;


    /**
     * 响应数据
     */
    private T data;

    /**
     * http状态码
     */
    private Integer httpStatus;


    /**
     * 响应时间
     */
    private long timestamp = System.currentTimeMillis();

    public Result() {
        super();
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @JSONField(serialize = false, deserialize = false)
    @JsonIgnore
    public Integer getHttpStatus() {
        return httpStatus;
    }

    @JSONField(serialize = false, deserialize = false)
    @JsonIgnore
    public boolean isOk() {
        return this.code.equals(ResultCode.SUCCESS.getCode());
    }


    public static Result ok() {
        return new Result().code(SUCCESS).msg(ResultCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> newResultData(T t) {
        return newResult().data(t);
    }

    public static <T> Result<T> newResultMsg(String msg) {
        return newResultMsgData(null, msg);
    }

    public static <T> Result<T> newResultMsgData(T t, String msg) {
        return newResult().data(t).msg(msg);
    }

    public static <T> Result<T> newResult() {
        return new Result<T>().code(SUCCESS).msg(ResultCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> newFailResult() {
        return newFailResult(ResultCode.FAILURE.getMessage());
    }

    public static <T> Result<T> newFailResult(String msg) {
        return new Result<T>().code(FAIL).msg(msg);
    }





    public static Result ok(String message) {
        return new Result().code(SUCCESS).msg(message);
    }

    public static Result failed() {
        return new Result().code(FAIL).msg(ResultCode.FAILURE.getMessage());
    }

    public static Result failed(Integer code,String message) {
        return new Result().code(code).msg(message);
    }

    public static Result failed(String message) {
        return new Result().code(FAIL).msg(message);
    }

    public static Result failed(ResultCode resultCode){
        return new Result().code(resultCode.getCode()).msg(resultCode.getMessage());
    }


    public Result code(Integer code) {
        this.code = code;
        return this;
    }

    public Result data(T data) {
        this.data = data;
        return this;
    }



    public Result httpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }


    public Result msg(String message) {
        this.message = message;
        return this;
    }

    public static <T> Result<T> condition(boolean flag) {
        return flag ? ok() : failed();
    }


    @Override
    public String toString() {
        return "ResultBody{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", httpStatus=" + httpStatus +
                ", timestamp=" + timestamp +
                '}';
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }


    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
