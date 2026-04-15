package com.travelvista.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travelvista.dto.OrderCreateDTO;
import com.travelvista.entity.Order;
import com.travelvista.entity.ScenicSpot;
import com.travelvista.exception.BusinessException;
import com.travelvista.mapper.OrderMapper;
import com.travelvista.service.OrderService;
import com.travelvista.service.ScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ScenicSpotService scenicSpotService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(Long userId, OrderCreateDTO dto) {
        ScenicSpot scenic = scenicSpotService.getById(dto.getScenicId());
        if (scenic == null || scenic.getStatus() == 0) {
            throw new BusinessException("景点不存在或已下架");
        }

        String orderNo = "TV" + IdUtil.getSnowflakeNextIdStr();

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setScenicId(dto.getScenicId());
        order.setScenicName(scenic.getName());
        order.setScenicImage(scenic.getCoverImage());
        order.setPrice(scenic.getPrice());
        order.setQuantity(dto.getQuantity());
        order.setTotalAmount(scenic.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
        order.setVisitorName(dto.getVisitorName());
        order.setVisitorPhone(dto.getVisitorPhone());
        order.setVisitDate(dto.getVisitDate());
        order.setStatus(0);

        save(order);

        return orderNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long userId, String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo).eq(Order::getUserId, userId);
        Order order = getOne(wrapper);

        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if (order.getStatus() != 0) {
            throw new BusinessException("订单状态不正确");
        }

        order.setStatus(1);
        order.setPayTime(LocalDateTime.now());
        updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long userId, String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo).eq(Order::getUserId, userId);
        Order order = getOne(wrapper);

        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if (order.getStatus() != 0) {
            throw new BusinessException("该订单无法取消");
        }

        order.setStatus(2);
        updateById(order);
    }

    @Override
    public Order getOrderDetail(Long userId, String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo).eq(Order::getUserId, userId);
        Order order = getOne(wrapper);

        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        return order;
    }

    @Override
    public IPage<Order> getUserOrders(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        return page(page, wrapper);
    }
}
