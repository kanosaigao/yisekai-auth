package com.yisekai.manager.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yisekai.manager.dao.entity.SysUserEntity;

/**
 * @author kano
 * @time 2025/7/1 13:51
 */
public interface SysUserService extends IService<SysUserEntity> {
    SysUserEntity getUserInfoByName(String name);

}
