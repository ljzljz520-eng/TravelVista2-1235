package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.dto.AdminLoginDTO;
import com.travelvista.entity.Admin;
import com.travelvista.entity.User;
import com.travelvista.vo.LoginVO;

public interface AdminService extends IService<Admin> {

    LoginVO login(AdminLoginDTO dto);

    IPage<User> getUserList(String keyword, Integer pageNum, Integer pageSize);

    void updateUserStatus(Long userId, Integer status);

    void deleteReview(Long reviewId);

    IPage<User> getAllUsers(Integer pageNum, Integer pageSize);
}
