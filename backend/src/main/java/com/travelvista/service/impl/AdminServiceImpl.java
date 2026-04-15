package com.travelvista.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.dto.LoginDTO;
import com.travelvista.entity.Admin;
import com.travelvista.exception.BusinessException;
import com.travelvista.mapper.AdminMapper;
import com.travelvista.service.AdminService;
import com.travelvista.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, loginDTO.getUsername());
        Admin admin = getOne(wrapper);
        
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        if (!admin.getPassword().equals(loginDTO.getPassword())) {
            throw new BusinessException("密码错误");
        }
        if (admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        
        String token = jwtUtils.generateToken(admin.getId(), admin.getUsername(), admin.getRole());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("admin", admin);
        return result;
    }
}
