package com.travelvista.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.dto.LoginDTO;
import com.travelvista.entity.User;
import com.travelvista.service.AdminService;
import com.travelvista.service.ReviewService;
import com.travelvista.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = adminService.login(loginDTO);
        return Result.success(result);
    }

    @GetMapping("/reviews")
    public Result<IPage<Map<String, Object>>> getReviews(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        IPage<Map<String, Object>> page = reviewService.getAllReviews(pageNum, pageSize, keyword);
        return Result.success(page);
    }

    @DeleteMapping("/review/{id}")
    public Result<Void> deleteReview(@PathVariable Long id) {
        reviewService.removeById(id);
        return Result.success();
    }

    @GetMapping("/users")
    public Result<IPage<User>> getUsers(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        IPage<User> page = userService.getAllUsers(pageNum, pageSize, keyword);
        return Result.success(page);
    }

    @PutMapping("/user/status")
    public Result<Void> updateUserStatus(@RequestParam Long id, @RequestParam Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        userService.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/user/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }
}
