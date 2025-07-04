package com.yisekai.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {

    SUCCESS(200, "操作成功"),
    FAILURE(400, "业务异常"),
    ERROR(500,"服务异常"),

    ;

    /**
     * 状态码
     */
    final Integer code;
    /**
     * 消息内容
     */
    final String message;

    public static ResultCode getResultEnum(Integer code) {
        for (ResultCode type : ResultCode.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return ERROR;
    }
}
