package com.travelvista.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.travelvista.common.Result;
import com.travelvista.entity.*;
import com.travelvista.mapper.*;
import com.travelvista.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/admin/statistics")
public class StatisticsController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ScenicSpotMapper scenicSpotMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @SaCheckLogin
    @GetMapping
    public Result<StatisticsVO> getStatistics() {
        StatisticsVO vo = new StatisticsVO();

        vo.setUserCount(userMapper.selectCount(null));
        vo.setScenicSpotCount(scenicSpotMapper.selectCount(null));
        vo.setReviewCount(reviewMapper.selectCount(null));

        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getStatus, 1);
        Long paidOrderCount = orderMapper.selectCount(orderWrapper);
        vo.setOrderCount(paidOrderCount);

        BigDecimal totalAmount = BigDecimal.ZERO;
        orderWrapper.clear();
        orderWrapper.eq(Order::getStatus, 1);
        var paidOrders = orderMapper.selectList(orderWrapper);
        for (Order order : paidOrders) {
            totalAmount = totalAmount.add(order.getTotalAmount());
        }
        vo.setTotalAmount(totalAmount);

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        LambdaQueryWrapper<Order> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.eq(Order::getStatus, 1)
                .between(Order::getPayTime, todayStart, todayEnd);
        Long todayOrderCount = orderMapper.selectCount(todayWrapper);
        vo.setTodayOrderCount(todayOrderCount);

        BigDecimal todayAmount = BigDecimal.ZERO;
        var todayOrders = orderMapper.selectList(todayWrapper);
        for (Order order : todayOrders) {
            todayAmount = todayAmount.add(order.getTotalAmount());
        }
        vo.setTodayAmount(todayAmount);

        return Result.success(vo);
    }
}
