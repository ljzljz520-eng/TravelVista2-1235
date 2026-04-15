package com.travelvista.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.travelvista.common.Result;
import com.travelvista.dto.OrderCreateDTO;
import com.travelvista.entity.Order;
import com.travelvista.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @SaCheckLogin
    @PostMapping("/create")
    public Result<String> createOrder(@Validated @RequestBody OrderCreateDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        String orderNo = orderService.createOrder(userId, dto);
        return Result.success("订单创建成功", orderNo);
    }

    @SaCheckLogin
    @PostMapping("/pay/{orderNo}")
    public Result<Void> payOrder(@PathVariable String orderNo) {
        Long userId = StpUtil.getLoginIdAsLong();
        orderService.payOrder(userId, orderNo);
        return Result.success("支付成功", null);
    }

    @SaCheckLogin
    @PostMapping("/cancel/{orderNo}")
    public Result<Void> cancelOrder(@PathVariable String orderNo) {
        Long userId = StpUtil.getLoginIdAsLong();
        orderService.cancelOrder(userId, orderNo);
        return Result.success("取消成功", null);
    }

    @SaCheckLogin
    @GetMapping("/detail/{orderNo}")
    public Result<Order> getOrderDetail(@PathVariable String orderNo) {
        Long userId = StpUtil.getLoginIdAsLong();
        Order order = orderService.getOrderDetail(userId, orderNo);
        return Result.success(order);
    }

    @SaCheckLogin
    @GetMapping("/list")
    public Result<IPage<Order>> getUserOrders(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = StpUtil.getLoginIdAsLong();
        IPage<Order> page = orderService.getUserOrders(userId, status, pageNum, pageSize);
        return Result.success(page);
    }
}
