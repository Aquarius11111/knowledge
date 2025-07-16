package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminService extends IService<Admin> {
    List<Admin> selectAll(Admin admin);
    PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize);
    Admin login(Admin admin);
    boolean updateByAccount(Admin admin);

    boolean removeByAccount(String account);

    Admin getByAccount(String account);
}