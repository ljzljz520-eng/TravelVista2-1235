package com.travelvista.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.dto.UserLoginDTO;
import com.travelvista.dto.UserRegisterDTO;
import com.travelvista.dto.UserUpdateDTO;
import com.travelvista.entity.User;
import com.travelvista.vo.LoginVO;

public interface UserService extends IService<User> {

    LoginVO login(UserLoginDTO dto);

    void register(UserRegisterDTO dto);

    void updateUserInfo(Long userId, UserUpdateDTO dto);

    User getUserInfo(Long userId);
}
