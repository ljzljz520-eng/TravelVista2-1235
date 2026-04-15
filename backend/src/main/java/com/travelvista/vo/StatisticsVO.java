package com.travelvista.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StatisticsVO {

    private Long userCount;

    private Long scenicSpotCount;

    private Long orderCount;

    private Long reviewCount;

    private BigDecimal totalAmount;

    private Long todayOrderCount;

    private BigDecimal todayAmount;
}
