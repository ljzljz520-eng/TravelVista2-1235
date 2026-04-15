package com.travelvista.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseEntity {
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private Integer gender;
    private Integer status;
}
