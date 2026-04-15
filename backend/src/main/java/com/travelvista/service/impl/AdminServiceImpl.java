package com.travelvista.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.dto.AdminLoginDTO;
import com.travelvista.entity.Admin;
import com.travelvista.entity.Review;
import com.travelvista.entity.User;
import com.travelvista.exception.BusinessException;
import com.travelvista.mapper.AdminMapper;
import com.travelvista.mapper.ReviewMapper;
import com.travelvista.mapper.UserMapper;
import com.travelvista.service.AdminService;
import com.travelvista.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public LoginVO login(AdminLoginDTO dto) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, dto.getUsername());
        Admin admin = getOne(wrapper);

        if (admin == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        if (!BCrypt.checkpw(dto.getPassword(), admin.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        StpUtil.login(admin.getId());
        String token = StpUtil.getTokenValue();

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(admin.getId());
        vo.setUsername(admin.getUsername());
        vo.setNickname(admin.getNickname());
        vo.setRole(admin.getRole());

        return vo;
    }

    @Override
    public IPage<User> getUserList(String keyword, Integer pageNum, Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getNickname, keyword)
                    .or()
                    .like(User::getPhone, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);
        return userMapper.selectPage(page, wrapper);
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        userMapper.updateById(user);
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException("评论不存在");
        }
        reviewMapper.deleteById(reviewId);
    }

    @Override
    public IPage<User> getAllUsers(Integer pageNum, Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreateTime);
        return userMapper.selectPage(page, wrapper);
    }
}
