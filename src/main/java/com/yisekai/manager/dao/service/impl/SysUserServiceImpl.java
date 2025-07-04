package com.yisekai.manager.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yisekai.manager.dao.entity.SysUserEntity;
import com.yisekai.manager.dao.mapper.SysUserMapper;
import com.yisekai.manager.dao.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @author kano
 * @time 2025/7/1 13:51
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {
    @Override
    public SysUserEntity getUserInfoByName(String name) {
       return this.getOne(new LambdaQueryWrapper<SysUserEntity>()
                .eq(SysUserEntity::getUserName,name));
    }
}
