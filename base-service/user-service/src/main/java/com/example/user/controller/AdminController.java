package com.example.user.controller;

import com.example.common.R;
import com.example.domain.Admin;
import com.example.user.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 新增
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public R add(@RequestBody Admin admin) {
        adminService.saveAdmin(admin);
        return R.success();
    }

    /**
     * 删除
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{account}")
    public R deleteById(@PathVariable String account) {
        adminService.removeByAccount(account);
        return R.success();
    }

    /**
     * 修改
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public R updateByAccount(@RequestBody Admin admin) {
        adminService.updateByAccount(admin);
        return R.success();
    }

    /**
     * 根据Account查询
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/selectByAccount/{account}")
    public R selectByAccount(@PathVariable String account) {
        Admin admin = adminService.getByAccount(account);
        return R.success(admin);
    }

    /**
     * 查询所有
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/selectAll")
    public R selectAll(Admin admin ) {
        List<Admin> list = adminService.selectAll(admin);
        return R.success(list);
    }

    /**
     * 分页查询
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/selectPage")
    public R selectPage(Admin admin,
                        @RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> page = adminService.selectPage(admin, pageNum, pageSize);
        return R.success(page);
    }


} 