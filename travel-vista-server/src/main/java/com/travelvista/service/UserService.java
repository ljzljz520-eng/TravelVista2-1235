package com.travelvista.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.entity.User;
import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> login(String username, String password);
    boolean register(User user);
    User getCurrentUser();
}
