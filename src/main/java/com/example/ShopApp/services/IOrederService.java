package com.example.ShopApp.services;

import com.example.ShopApp.dtos.OrderDTO;
import com.example.ShopApp.models.Order;
import com.example.ShopApp.responses.OrderResponse;

import java.util.List;

public interface IOrederService {
    Order creatOrder(OrderDTO orderDTO) throws Exception;

    Order getOrderById(Long id);

    Order updateOrder(Long id, OrderDTO orderDTO);

    void deleteOrderById(Long id);

    List<Order> getAllOrders(Long userId);
}
