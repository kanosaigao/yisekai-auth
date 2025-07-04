package com.yisekai.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author kano
 * @time 2025/3/26 11:38
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "token")
public class TokenConfig {


    private String header;


    private String secret;

    private int expireTime;
}
