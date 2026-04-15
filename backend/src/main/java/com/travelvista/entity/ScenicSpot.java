package com.travelvista.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("scenic_spot")
public class ScenicSpot extends BaseEntity {
    private String name;
    private String description;
    private String images;
    private String coverImage;
    private String province;
    private String city;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String type;
    private String level;
    private BigDecimal ticketPrice;
    private String openTime;
    private String phone;
    private BigDecimal rating;
    private Integer viewCount;
    private Integer reviewCount;
    private Integer status;
}
