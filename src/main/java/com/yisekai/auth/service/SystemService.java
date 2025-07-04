package com.yisekai.auth.service;

import com.yisekai.auth.dto.LoginBody;
import com.yisekai.auth.dto.req.RegisterReq;

import java.util.Map;

/**
 * @author kano
 * @time 2025/7/1 14:04
 */
public interface SystemService {
    Map<String,Object> login(LoginBody loginBody);

    void register(RegisterReq req);

}
