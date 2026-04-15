package com.travelvista.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.dto.LoginDTO;
import com.travelvista.dto.RegisterDTO;
import com.travelvista.entity.User;
import com.travelvista.exception.BusinessException;
import com.travelvista.mapper.UserMapper;
import com.travelvista.service.UserService;
import com.travelvista.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = getOne(wrapper);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new BusinessException("密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), "user");
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException("两次密码不一致");
        }
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        User existUser = getOne(wrapper);
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setStatus(1);
        save(user);
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public void updateUserInfo(User user) {
        updateById(user);
    }

    @Override
    public IPage<User> getAllUsers(Integer pageNum, Integer pageSize, String keyword) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getNickname, keyword)
                    .or().like(User::getEmail, keyword));
        }
        wrapper.orderByDesc(User::getCreateTime);
        
        IPage<User> userPage = page(page, wrapper);
        userPage.getRecords().forEach(u -> u.setPassword(null));
        return userPage;
    }
}
