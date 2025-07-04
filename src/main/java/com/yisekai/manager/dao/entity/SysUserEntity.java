package com.yisekai.manager.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author kano
 * @time 2025/7/1 13:39
 */
@Data
@TableName("sys_user")
public class SysUserEntity extends BaseEntity{
    private String userName;

    private String realName;

    private String userType;

    private String email;

    private String phoneNumber;

    private String sex;

    private String avatar;

    private String password;

    private String status;

    private String loginIp;


    private Date loginDate;

    private String remark;

}
