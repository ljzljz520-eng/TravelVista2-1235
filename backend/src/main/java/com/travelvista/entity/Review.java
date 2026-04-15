package com.travelvista.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("review")
public class Review extends BaseEntity {
    private Long userId;
    private Long scenicId;
    private String content;
    private String images;
    private Integer rating;
    private Integer status;
}
