package com.travelvista.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("scenic_spot")
public class ScenicSpot {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String coverImage;
    private String images;
    private String video;
    private String description;
    private String content;
    private String province;
    private String city;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String type;
    private String level;
    private BigDecimal ticketPrice;
    private String openTime;
    private String visitTime;
    private BigDecimal rating;
    private Integer reviewCount;
    private Integer viewCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
