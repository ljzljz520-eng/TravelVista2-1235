package com.travelvista.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.dto.UserLoginDTO;
import com.travelvista.dto.UserRegisterDTO;
import com.travelvista.dto.UserUpdateDTO;
import com.travelvista.entity.User;
import com.travelvista.exception.BusinessException;
import com.travelvista.mapper.UserMapper;
import com.travelvista.service.UserService;
import com.travelvista.vo.LoginVO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public LoginVO login(UserLoginDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = getOne(wrapper);
        
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        
        String md5Password = SaSecureUtil.md5(dto.getPassword());
        if (!md5Password.equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        LoginVO vo = new LoginVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setToken(token);
        
        return vo;
    }

    @Override
    public void register(UserRegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User existUser = getOne(wrapper);
        
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(SaSecureUtil.md5(dto.getPassword()));
        user.setNickname(StrUtil.isNotBlank(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setStatus(1);
        user.setGender(0);
        
        save(user);
    }

    @Override
    public void updateUserInfo(Long userId, UserUpdateDTO dto) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        if (StrUtil.isNotBlank(dto.getNickname())) {
            user.setNickname(dto.getNickname());
        }
        if (StrUtil.isNotBlank(dto.getAvatar())) {
            user.setAvatar(dto.getAvatar());
        }
        if (StrUtil.isNotBlank(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        if (StrUtil.isNotBlank(dto.getPhone())) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        
        updateById(user);
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
}
