package com.example.user.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.R;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.domain.Account;
import com.example.domain.Admin;
import com.example.domain.User;
import com.example.user.config.TokenUtils;
import com.example.user.service.AdminService;
import com.example.user.service.UserService;
import com.example.user.utils.UsernameGenerator;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/**
 * 基础前端接口
 */
@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Autowired
    private TokenUtils tokenUtils;



    /**
     * 登录
     */
    @PostMapping("/login")
    public R login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getAccount()) || ObjectUtil.isEmpty(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return R.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            Admin admin = new Admin();
            admin.setAccount(account.getAccount());
            admin.setPassword(account.getPassword());
            Admin loginAdmin = adminService.login(admin);
            if (loginAdmin == null) return R.error(ResultCodeEnum.USER_ACCOUNT_ERROR);
            account.setId(loginAdmin.getAdminId());
            account.setRole(loginAdmin.getRole());
            account.setUsername(loginAdmin.getUsername());
            // 生成token
            String token = tokenUtils.generateToken(loginAdmin.getAccount().toString()+"-ADMIN");
            account.setToken(token);
            return R.success(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            User user = new User();
            user.setAccount(account.getAccount());
            user.setPassword(account.getPassword());
            User loginUser = userService.login(user);
            if (loginUser == null) return R.error(ResultCodeEnum.USER_ACCOUNT_ERROR);
            account.setId(loginUser.getUserId());
            account.setRole(loginUser.getRole());
            account.setUsername(loginUser.getUsername());
            // 生成token
            String token = tokenUtils.generateToken(loginUser.getAccount().toString()+"-USER");
            account.setToken(token);
            return R.success(account);
        }
        return R.error(ResultCodeEnum.USER_ACCOUNT_ERROR);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public R register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getEmail()) || StrUtil.isBlank(account.getPassword())
                || StrUtil.isBlank(account.getAccount())
                || ObjectUtil.isEmpty(account.getRole())) {
            return R.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            if(adminService.getByAccount(account.getAccount())!=null){
                return R.error(ResultCodeEnum.USER_EXIST_ERROR);
            }
            Admin admin = new Admin();
            admin.setAccount(account.getAccount());
            admin.setEmail(account.getEmail());
            admin.setUsername(UsernameGenerator.generateUsername());
            admin.setPassword(account.getPassword());
            admin.setRole(account.getRole());
            adminService.save(admin);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            if(userService.getByAccount(account.getAccount())!=null){
                return R.error(ResultCodeEnum.USER_EXIST_ERROR);
            }
            User user = new User();
            user.setAccount(account.getAccount());
            user.setPassword(account.getPassword());
            user.setUsername(UsernameGenerator.generateUsername());
            user.setEmail(account.getEmail());
            user.setRole(account.getRole());
            user.setStatus(true);
            userService.save(user);
        }
        return R.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public R updatePassword(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getAccount()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return R.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            Admin admin = new Admin();
            admin.setAccount(account.getAccount());
            admin.setPassword(account.getPassword());
            if(adminService.login(admin)==null){
                return R.error(ResultCodeEnum.PARAM_PASSWORD_ERROR);
            }
            admin.setPassword(account.getNewPassword());
            adminService.updateByAccount(admin);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            User user = new User();
            user.setAccount(account.getAccount());
            user.setPassword(account.getPassword());
            if(userService.login(user)==null){
                return R.error(ResultCodeEnum.PARAM_PASSWORD_ERROR);
            }
            user.setPassword(account.getNewPassword());
            userService.updateByAccount(user);
        }
        return R.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/forgot")
    public R forgot(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getRole()) || StrUtil.isBlank(account.getEmail())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return R.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            Admin admin = adminService.getByAccount(account.getAccount());
            if(admin==null){
                return R.error(ResultCodeEnum.USER_NOT_EXIST_ERROR);
            }
            if(!account.getEmail().equals(admin.getEmail())){
                return R.error(ResultCodeEnum.EMAIL_ERROR);
            }
            admin.setPassword(account.getNewPassword());
            adminService.updateByAccount(admin);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            User user = userService.getByAccount(account.getAccount());
            if(user==null){
                return R.error(ResultCodeEnum.USER_NOT_EXIST_ERROR);
            }
            if(!account.getEmail().equals(user.getEmail())){
                return R.error(ResultCodeEnum.EMAIL_ERROR);
            }
            user.setPassword(account.getNewPassword());
            userService.updateByAccount(user);
        }
        return R.success();
    }

}