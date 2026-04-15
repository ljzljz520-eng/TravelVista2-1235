package com.travelvista.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.dto.LoginDTO;
import com.travelvista.entity.Admin;

import java.util.Map;

public interface AdminService extends IService<Admin> {
    Map<String, Object> login(LoginDTO loginDTO);
}
