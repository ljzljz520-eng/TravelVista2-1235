package com.travelvista.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ScenicQueryDTO {

    private String keyword;

    private Long categoryId;

    private String province;

    private String city;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private BigDecimal minRating;

    private String sortBy;

    private String sortOrder;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
