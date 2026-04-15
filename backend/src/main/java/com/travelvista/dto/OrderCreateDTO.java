package com.travelvista.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class OrderCreateDTO {

    @NotNull(message = "景点ID不能为空")
    private Long scenicId;

    @NotNull(message = "购买数量不能为空")
    private Integer quantity;

    @NotBlank(message = "游客姓名不能为空")
    private String visitorName;

    @NotBlank(message = "游客电话不能为空")
    private String visitorPhone;

    @NotNull(message = "游玩日期不能为空")
    private LocalDate visitDate;
}
