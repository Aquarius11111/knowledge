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

    /**
     * 生成token
     */
//    public static String genToken(String data) {
//        return JWT.create().withAudience(data) // 将 user-Account 保存到 token 里面,作为载荷
//                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2小时后token过期
//                .sign(Algorithm.HMAC256(SALT)); // 以 password 作为 token 的密钥
//    }

    public String generateToken(String data) {
        Date now = new Date();
        System.out.println("================login-key:"+jwtKeyConfig.getCurrentSecret());
        Date expire = new Date(now.getTime() + jwtProperties.getExpirationMinutes() * 60 * 1000L);
        return JWT.create()
                .withAudience(data)
                .withIssuedAt(now)
                .withExpiresAt(expire)
                .sign(Algorithm.HMAC256(jwtKeyConfig.getCurrentSecret()));
    }

    /**
     * 获取当前登录的用户ID
     */
//    public static String getCurrentAccount() {
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            String token = request.getHeader(TOKEN);
//            if (ObjectUtil.isNotEmpty(token)) {
//                String userRole = JWT.decode(token).getAudience().get(0);
//                return userRole.split("-")[0];  // 获取用户account
//            }
//        } catch (Exception e) {
//            log.error("获取当前用户account出错", e);
//        }
//        return null;
//    }

    /**
     * 获取当前登录的用户角色
     */
//    public static String getCurrentRole() {
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            String token = request.getHeader(TOKEN);
//            if (ObjectUtil.isNotEmpty(token)) {
//                String userRole = JWT.decode(token).getAudience().get(0);
//                return userRole.split("-")[1];  // 获取角色
//            }
//        } catch (Exception e) {
//            log.error("获取当前用户角色出错", e);
//        }
//        return null;
//    }

    /**
     * 验证token是否有效
     */
//    public static boolean verifyToken(String token) {
//        try {
//            JWT.require(Algorithm.HMAC256(SALT)).build().verify(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

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


    /**
     * 从token中获取用户角色信息
     */
//    public static String getUserRoleFromToken(String token) {
//        try {
//            String userRole = JWT.decode(token).getAudience().get(0);
//            return userRole.split("-")[1];
//        } catch (JWTDecodeException e) {
//            return null;
//        }
//    }

    /**
     * 从token中获取用户ID
     */
//    public static String getUserIdFromToken(String token) {
//        try {
//            String userRole = JWT.decode(token).getAudience().get(0);
//            return userRole.split("-")[0];
//        } catch (JWTDecodeException e) {
//            return null;
//        }
//    }
}

