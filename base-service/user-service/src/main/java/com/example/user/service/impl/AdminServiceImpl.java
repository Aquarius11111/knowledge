package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.Admin;
import com.example.domain.User;
import com.example.mapper.AdminMapper;
import com.example.user.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public List<Admin> selectAll(Admin admin) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (admin != null) {
            if (admin.getAccount() != null) {
                queryWrapper.like("account", admin.getAccount());
            }
        }
        return adminMapper.selectList(queryWrapper);
    }

    @Override
    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = selectAll(admin);
        return PageInfo.of(list);
    }

    @Override
    public Admin login(Admin admin) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", admin.getAccount())
                   .eq("password", admin.getPassword());
        return adminMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean updateByAccount(Admin admin) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        admin.setUpdatetime(new Date());
        queryWrapper.eq("account", admin.getAccount());

        return this.update(admin, queryWrapper);
    }

    @Override
    public boolean  removeByAccount(String account) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return adminMapper.delete(queryWrapper) > 0;
    }

    @Override
    public Admin getByAccount(String account) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return adminMapper.selectOne(queryWrapper);
    }

} 