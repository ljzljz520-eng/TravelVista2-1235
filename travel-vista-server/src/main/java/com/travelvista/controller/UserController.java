package com.travelvista.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.travelvista.common.Result;
import com.travelvista.entity.User;
import com.travelvista.service.UserService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        try {
            Map<String, Object> result = userService.login(username, password);
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody User user) {
        try {
            boolean result = userService.register(user);
            return Result.success("注册成功", result);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/info")
    @SaCheckLogin
    public Result<User> getUserInfo() {
        User user = userService.getCurrentUser();
        return Result.success(user);
    }

    @PutMapping("/update")
    @SaCheckLogin
    public Result<Boolean> updateUserInfo(@RequestBody User user) {
        user.setId(null);
        user.setUsername(null);
        user.setPassword(null);
        user.setStatus(null);
        user.setCreateTime(null);
        user.setUpdateTime(null);
        
        Long userId = Long.valueOf(cn.dev33.satoken.stp.StpUtil.getLoginId().toString());
        user.setId(userId);
        
        boolean result = userService.updateById(user);
        return Result.success("更新成功", result);
    }

    @PostMapping("/logout")
    @SaCheckLogin
    public Result<Void> logout() {
        cn.dev33.satoken.stp.StpUtil.logout();
        return Result.success();
    }
}
