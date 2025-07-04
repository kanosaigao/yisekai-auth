package com.yisekai.auth.controller;

import com.yisekai.auth.domain.Result;
import com.yisekai.auth.dto.LoginBody;
import com.yisekai.auth.dto.req.RegisterReq;
import com.yisekai.auth.service.SystemService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author kano
 * @time 2025/7/1 10:52
 */
@RestController
@RequestMapping("/system")
public class SystemController {
    @Resource
    private SystemService systemService;

    /**
     * 登录
     * @param loginBody
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginBody loginBody){
        Map<String, Object> map = systemService.login(loginBody);
        return Result.ok("登录成功").data(map);
    }

    /**
     * 用户注册
     * @param req
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterReq req){
        systemService.register(req);
        return Result.ok("注册成功");
    }
}
