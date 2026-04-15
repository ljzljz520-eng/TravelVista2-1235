package com.travelvista.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long scenicSpotId;
    private String content;
    private String images;
    private Integer rating;
    private Integer status;
    private Integer likeCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
