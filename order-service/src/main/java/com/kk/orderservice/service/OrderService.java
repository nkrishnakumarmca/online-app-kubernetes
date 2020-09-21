package com.kk.orderservice.service;

import com.kk.orderservice.exception.OrderExistsException;
import com.kk.orderservice.exception.OrderNotFoundException;
import com.kk.orderservice.model.Order;

public interface OrderService {
    Order addNewOrder(Order order) throws OrderExistsException;

    Order getOrderByOrderId(String orderId) throws OrderNotFoundException;
}
