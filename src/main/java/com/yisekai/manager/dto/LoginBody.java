package com.yisekai.manager.dto;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author kano
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;


}
