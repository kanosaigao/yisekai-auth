package com.yisekai.manager.security.handle;

import com.alibaba.fastjson2.JSON;
import com.yisekai.manager.auth.TokenService;
import com.yisekai.manager.common.HttpStatus;
import com.yisekai.manager.domain.Result;
import com.yisekai.manager.dto.LoginUser;
import com.yisekai.manager.util.ServletUtils;
import com.yisekai.manager.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            ServletUtils.renderString(response, JSON.toJSONString(Result.failed(HttpStatus.SUCCESS, "退出成功")));
        }
    }

}
