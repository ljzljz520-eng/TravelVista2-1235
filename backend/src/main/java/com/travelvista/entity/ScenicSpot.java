package com.travelvista.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("scenic_spot")
public class ScenicSpot {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long categoryId;

    private String description;

    private String content;

    private String coverImage;

    private String images;

    private String video;

    private String address;

    private String province;

    private String city;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private BigDecimal price;

    private String openTime;

    private String visitTime;

    private BigDecimal rating;

    private Integer reviewCount;

    private Integer viewCount;

    private Integer favoriteCount;

    private Integer status;

    private Integer isHot;

    private Integer isRecommended;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
