package com.yisekai.manager.dto.req;

import lombok.Data;

/**
 * @author kano
 * @time 2025/7/1 17:24
 */
@Data
public class RegisterReq {

    private String userName;

    private String password;

    private String email;

    private String phoneNumber;

    private String sex;

}
