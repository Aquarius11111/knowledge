package com.example.user.controller;

import com.example.common.R;
import com.example.common.enums.ResultCodeEnum;
import com.example.domain.User;
import com.example.user.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 用户前端操作接口
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 新增
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public R add(@RequestBody User user) {
        if(userService.getByAccount(user.getAccount())!=null){
            return R.error(ResultCodeEnum.USER_EXIST_ERROR);
        }
        userService.save(user);
        return R.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{account}")
    public R deleteById(@PathVariable String account) {
        userService.removeByAccount(account);
        return R.success();
    }

    /**
     * 批量删除
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/batch")
    public R deleteBatch(@RequestBody List<String> accounts) {
        userService.removeBatchByAccounts(accounts);
        return R.success();
    }

    /**
     * 修改
     */

    @PutMapping("/update")
    public R updateByAccount(@RequestBody User user) {
        userService.updateByAccount(user);
        return R.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectByAccount/{account}")
    public R selectById(@PathVariable String account) {
        User user = userService.getByAccount(account);
        return R.success(user);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public R selectAll(User user ) {
        List<User> list = userService.selectAll(user);
        return R.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public R selectPage(User user,
                        @RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<User> page = userService.selectPage(user, pageNum, pageSize);
        return R.success(page);
    }

} 