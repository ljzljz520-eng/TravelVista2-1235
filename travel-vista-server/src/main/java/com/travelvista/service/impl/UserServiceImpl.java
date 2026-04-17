package com.travelvista.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.entity.User;
import com.travelvista.mapper.UserMapper;
import com.travelvista.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Map<String, Object> login(String username, String password) {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new RuntimeException("用户名和密码不能为空");
        }
        
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = getOne(queryWrapper);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }
        
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        user.setPassword(null);
        
        return result;
    }

    @Override
    public boolean register(User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            throw new RuntimeException("用户名和密码不能为空");
        }
        
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        Long count = count(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setStatus(1);
        if (StrUtil.isBlank(user.getNickname())) {
            user.setNickname(user.getUsername());
        }
        
        return save(user);
    }

    @Override
    public User getCurrentUser() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = getById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
}
