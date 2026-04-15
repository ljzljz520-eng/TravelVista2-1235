package com.travelvista.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travelvista.dto.OrderCreateDTO;
import com.travelvista.entity.Order;

public interface OrderService extends IService<Order> {

    String createOrder(Long userId, OrderCreateDTO dto);

    void payOrder(Long userId, String orderNo);

    void cancelOrder(Long userId, String orderNo);

    Order getOrderDetail(Long userId, String orderNo);

    IPage<Order> getUserOrders(Long userId, Integer status, Integer pageNum, Integer pageSize);
}
