package com.yisekai.manager.enums;

import lombok.Getter;

/**
 * @author kano
 * @time 2025/7/1 14:25
 */
@Getter
public enum StatusEnum {

    ENABLE("0","启用"),

    DISABLE("1","停用")

    ;
    private final String code;

    private final String message;

    StatusEnum(String  code, String message) {
        this.code = code;
        this.message = message;
    }
}
