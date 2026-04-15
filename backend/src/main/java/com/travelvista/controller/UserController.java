package com.travelvista.controller;

import com.travelvista.common.Result;
import com.travelvista.dto.LoginDTO;
import com.travelvista.dto.RegisterDTO;
import com.travelvista.entity.User;
import com.travelvista.service.UserService;
import com.travelvista.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = userService.login(loginDTO);
        return Result.success(result);
    }

    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success();
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtils.getUserId(token);
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result<Void> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtils.getUserId(token);
        user.setId(userId);
        userService.updateUserInfo(user);
        return Result.success();
    }
}
