package com.travelvista.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ReviewAddDTO {

    @NotNull(message = "景点ID不能为空")
    private Long scenicId;

    private String content;

    private String images;

    @NotNull(message = "评分不能为空")
    private BigDecimal rating;
}
