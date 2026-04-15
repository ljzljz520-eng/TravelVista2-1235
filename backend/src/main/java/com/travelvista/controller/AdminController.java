package com.travelvista.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.dto.AdminLoginDTO;
import com.travelvista.entity.Admin;
import com.travelvista.entity.User;
import com.travelvista.service.AdminService;
import com.travelvista.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody AdminLoginDTO dto) {
        LoginVO vo = adminService.login(dto);
        return Result.success("登录成功", vo);
    }

    @SaCheckLogin
    @PostMapping("/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success("退出成功", null);
    }

    @SaCheckLogin
    @GetMapping("/info")
    public Result<Admin> getAdminInfo() {
        Long adminId = StpUtil.getLoginIdAsLong();
        Admin admin = adminService.getById(adminId);
        admin.setPassword(null);
        return Result.success(admin);
    }

    @SaCheckLogin
    @GetMapping("/user/list")
    public Result<IPage<User>> getUserList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<User> page = adminService.getUserList(keyword, pageNum, pageSize);
        return Result.success(page);
    }

    @SaCheckLogin
    @PutMapping("/user/status/{userId}")
    public Result<Void> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam Integer status) {
        adminService.updateUserStatus(userId, status);
        return Result.success("状态更新成功", null);
    }

    @SaCheckLogin
    @DeleteMapping("/review/{reviewId}")
    public Result<Void> deleteReview(@PathVariable Long reviewId) {
        adminService.deleteReview(reviewId);
        return Result.success("删除成功", null);
    }
}
