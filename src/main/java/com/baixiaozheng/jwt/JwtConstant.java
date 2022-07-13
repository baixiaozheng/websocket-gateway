package com.baixiaozheng.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConstant {
    public static final String JWT_ID = "098f6bcd4621d373cade4e832627b4f6";
    public static final String JWT_NAME = "api.richman.com";
    public static final String JWT_SECRET = "HnNhZYwY13pHZZlpcMPdmHuSAeXOxYPFQDzxiAH6Heb";
    public static final int JWT_TTL = 3600000;
    public static final int JWT_REFRESH_INTERVAL = 3300000;
    public static long JWT_REFRESH_TTL = 28800000L;

    public JwtConstant() {
    }

    @Value("${token.expire}")
    public void setExpire(Long expire) {
        if (expire != null) {
            JWT_REFRESH_TTL = Long.valueOf(expire);
        }

    }
}