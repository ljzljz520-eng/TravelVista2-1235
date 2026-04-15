package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.dto.LoginDTO;
import com.travelvista.dto.RegisterDTO;
import com.travelvista.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> login(LoginDTO loginDTO);
    void register(RegisterDTO registerDTO);
    User getUserInfo(Long userId);
    void updateUserInfo(User user);
    IPage<User> getAllUsers(Integer pageNum, Integer pageSize, String keyword);
}
