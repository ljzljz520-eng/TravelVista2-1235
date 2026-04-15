package com.travelvista.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("favorite")
public class Favorite implements Serializable {
    private Long id;
    private Long userId;
    private Long scenicId;
    private LocalDateTime createTime;
}
