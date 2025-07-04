package com.yisekai.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yisekai.auth.auth.TokenService;
import com.yisekai.auth.exception.ServiceException;
import com.yisekai.auth.common.Constants;
import com.yisekai.auth.dao.entity.SysUserEntity;
import com.yisekai.auth.dao.service.SysUserService;
import com.yisekai.auth.dto.LoginBody;
import com.yisekai.auth.dto.LoginUser;
import com.yisekai.auth.dto.req.RegisterReq;
import com.yisekai.auth.enums.StatusEnum;
import com.yisekai.auth.service.SystemService;
import com.yisekai.auth.util.IpUtils;
import com.yisekai.auth.util.SecurityUtils;
import com.yisekai.auth.util.ServletUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kano
 * @time 2025/7/1 14:05
 */
@Service
public class SystemServiceImpl implements SystemService {
    @Resource
    private SysUserService sysUserService;

    @Resource
    private TokenService tokenService;


    @Override
    public Map<String, Object> login(LoginBody loginBody) {
        Map<String , Object> map = new HashMap<>();
        SysUserEntity sysUser = sysUserService.getUserInfoByName(loginBody.getUserName());
        if (ObjectUtil.isEmpty(sysUser)){
            throw new ServiceException("用户不存在！");
        }
        if (ObjectUtil.equal(sysUser.getStatus(), StatusEnum.DISABLE.getCode())){
            throw new ServiceException("用户已被停用！");
        }
        if (!SecurityUtils.matchesPassword(loginBody.getPassword(),sysUser.getPassword())) {
            throw new ServiceException("密码错误！");
        }
        LoginUser loginUser = new LoginUser(sysUser.getId(), sysUser);
        String token = tokenService.createToken(loginUser);
        map.put(Constants.TOKEN,token);
        //记录登录信息
        recordLoginInfo(sysUser);
        return map;
    }

    @Override
    public void register(RegisterReq req) {
        SysUserEntity sysUser = sysUserService.getUserInfoByName(req.getUserName());
        if (ObjectUtil.isNotEmpty(sysUser)){
            throw new ServiceException("用户已经存在！");
        }
        req.setPassword(SecurityUtils.encryptPassword(req.getPassword()));
        req.setSex("2");
        sysUserService.save(BeanUtil.copyProperties(req,SysUserEntity.class));
    }

    private void recordLoginInfo(SysUserEntity sysUser){
        SysUserEntity loginUserinfo = new SysUserEntity();
        loginUserinfo.setId(sysUser.getId());
        loginUserinfo.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        loginUserinfo.setLoginDate(DateUtil.date());
        sysUserService.updateById(loginUserinfo);
    }
}
