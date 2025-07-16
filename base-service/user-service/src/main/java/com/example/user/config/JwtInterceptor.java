package com.example.user.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.exception.CustomException;
import com.example.user.service.AdminService;
import com.example.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * jwt拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final String TOKEN = "token";
    @Resource
    private  AdminService adminService;
    @Resource
    private  UserService userService;
    @Resource
    private TokenUtils tokenUtils;

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 1. 从http请求的header中获取token
        String token = request.getHeader(TOKEN);
        if (ObjectUtil.isEmpty(token)) {
            // 如果没拿到，从参数里再拿一次
            token = request.getParameter(TOKEN);
        }
        // 2. 开始执行认证
        if (ObjectUtil.isEmpty(token)) {
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }
        
        try {
            if(!tokenUtils.verifyToken(token)){
                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
            }

            // 解析token获取存储的数据
            String userRole = JWT.decode(token).getAudience().get(0);
            String account = userRole.split("-")[0];
            String role = userRole.split("-")[1];

            // 验证token是否有效
            if(RoleEnum.ADMIN.name().equals(role)&&adminService.getByAccount(account)==null){
                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
            } else if (RoleEnum.USER.name().equals(role)&&userService.getByAccount(account)==null) {
                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
            }else if(!RoleEnum.ADMIN.name().equals(role)&&!RoleEnum.USER.name().equals(role)){
                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
            }
            // 将用户信息存储到request中，供后续使用
            request.setAttribute("userAccount", account);
            request.setAttribute("userRole", role);
            
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        
        return true;
    }
}