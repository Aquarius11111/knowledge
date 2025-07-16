package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.Account;
import com.example.domain.Admin;
import com.example.domain.User;
import com.example.mapper.UserMapper;
import com.example.user.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> selectAll(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (user != null) {
            if (user.getAccount() != null) {
                queryWrapper.like("account", user.getAccount());
            }
        }
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = selectAll(user);
        return PageInfo.of(list);
    }

    @Override
    public User login(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", user.getAccount())
                   .eq("password", user.getPassword());
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean updateByAccount(User user){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", user.getAccount());

        return this.update(user, queryWrapper);
    }

    @Override
    public User getByAccount(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean removeByAccount(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        return userMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean removeBatchByAccounts(List<String> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return false; // 避免空列表导致的问题
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("account", accounts);

        // 执行批量删除操作并返回影响的行数是否大于0
        return userMapper.delete(queryWrapper) > 0;
    }

} 