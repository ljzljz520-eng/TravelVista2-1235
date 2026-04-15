package com.travelvista.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("admin")
public class Admin implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String role;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
