package com.travelvista.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserUpdateDTO {

    @Size(max = 20, message = "昵称长度不能超过20")
    private String nickname;

    private String avatar;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private Integer gender;
}
