package com.travelvista.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.travelvista.common.Result;
import com.travelvista.dto.UserLoginDTO;
import com.travelvista.dto.UserRegisterDTO;
import com.travelvista.dto.UserUpdateDTO;
import com.travelvista.entity.User;
import com.travelvista.service.UserService;
import com.travelvista.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody UserLoginDTO dto) {
        LoginVO vo = userService.login(dto);
        return Result.success("登录成功", vo);
    }

    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody UserRegisterDTO dto) {
        userService.register(dto);
        return Result.success("注册成功", null);
    }

    @SaCheckLogin
    @PostMapping("/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success("退出成功", null);
    }

    @SaCheckLogin
    @GetMapping("/info")
    public Result<User> getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    @SaCheckLogin
    @PutMapping("/update")
    public Result<Void> updateUserInfo(@Validated @RequestBody UserUpdateDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        userService.updateUserInfo(userId, dto);
        return Result.success("更新成功", null);
    }
}
