package com.example.user.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Token工具类
 */
@Component
public class TokenUtils {


    private static final String TOKEN = "token";
    private static final String SALT = "Champion";

    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    @Resource
    private JwtKeyConfig jwtKeyConfig;

    @Autowired
    private JwtProperties jwtProperties;

    public String generateToken(String data) {
        Date now = new Date();
        System.out.println("================login-key:"+jwtKeyConfig.getCurrentSecret());
        Date expire = new Date(now.getTime() + jwtProperties.getExpirationMinutes() * 60 * 1000L);
        return JWT.create()
                .withAudience(data)
                .withClaim("role", data.split("-")[1])
                .withIssuedAt(now)
                .withExpiresAt(expire)
                .sign(Algorithm.HMAC256(jwtKeyConfig.getCurrentSecret()));
    }



    public boolean verifyToken(String token) {
        try {
            // 使用当前秘钥验证
            System.out.println("==============当前key:"+jwtKeyConfig.getCurrentSecret());
            JWT.require(Algorithm.HMAC256(jwtKeyConfig.getCurrentSecret()))
                    .build().verify(token);
            return true;
        } catch (Exception e) {
            // 尝试使用旧秘钥验证
            try {
                System.out.println("==============旧key:"+jwtKeyConfig.getOldSecret());
                JWT.require(Algorithm.HMAC256(jwtKeyConfig.getOldSecret()))
                        .build().verify(token);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }


}

