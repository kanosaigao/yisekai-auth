package com.yisekai.manager.auth;

import cn.hutool.core.lang.UUID;
import com.yisekai.manager.common.Constants;
import com.yisekai.manager.config.TokenConfig;
import com.yisekai.manager.util.IpUtils;
import com.yisekai.manager.util.StringUtils;
import com.yisekai.manager.dto.LoginUser;
import com.yisekai.manager.dto.LoginUserToken;
import com.yisekai.manager.util.RedisUtil;
import com.yisekai.manager.util.ServletUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 * (根据若依修改，增加LoginUser)
 *
 * @author oddfar
 */
@Slf4j
@Component
public class TokenService {

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private TokenConfig tokenConfig;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        LoginUserToken loginUserToken = getLoginUserToken(request);
        if (StringUtils.isNotNull(loginUserToken)) {
            return redisUtil.getCacheObject(getLoginKey(loginUserToken.getUserId()));
        }
        return null;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUserToken getLoginUserToken(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                //获取用户id
                return redisUtil.getCacheObject(userKey);
            } catch (Exception e) {
                log.error("获取用户身份信息异常：{}", e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUserByUserId(long userId) {

        String loginKey = getLoginKey(userId);
        if (redisUtil.hasKey(loginKey)) {
            try {
                LoginUser user = redisUtil.getCacheObject(getLoginKey(userId));
                return user;
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisUtil.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = UUID.fastUUID().toString();
        loginUser.setToken(token);
        //设置登录信息
        LoginUserToken loginUserToken = LoginUserToken.builder()
                .userId(loginUser.getUserId())
                .token(token).build();
        setUserAgent(loginUserToken);
        refreshToken(loginUser, loginUserToken);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);

        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        String userKey = getTokenKey(loginUser.getToken());
        LoginUserToken loginUserToken = redisUtil.getCacheObject(userKey);
        refreshToken(loginUser, loginUserToken);
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser, LoginUserToken loginUserToken) {
        int expireTime = tokenConfig.getExpireTime();
        loginUserToken.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUserToken.getLoginTime() + expireTime * MILLIS_MINUTE);
        loginUserToken.setExpireTime(loginUserToken.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser登录的缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisUtil.setCacheObject(userKey, loginUserToken, expireTime, TimeUnit.MINUTES);
        //设置loginUser缓存
        redisUtil.setCacheObject(getLoginKey(loginUser.getUserId()), loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUserToken 登录信息
     */
    public void setUserAgent(LoginUserToken loginUserToken) {
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUserToken.setIpaddr(ip);

    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, tokenConfig.getSecret()).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenConfig.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenConfig.getHeader());
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }

    private String getLoginKey(Long userId) {
        return Constants.LOGIN_USER_KEY + userId;
    }
}
