package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.User;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService extends IService<User> {
    List<User> selectAll(User user);
    PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize);
    User login(User user);

    @Transactional
    boolean saveUser(User user);

    boolean removeByAccount(String account);

    boolean removeBatchByAccounts(List<String> accounts);

    boolean updateByAccount(User user);

    User getByAccount(String account);
}