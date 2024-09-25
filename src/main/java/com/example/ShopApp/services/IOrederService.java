package com.example.ShopApp.services;

import com.example.ShopApp.dtos.OrderDTO;
import com.example.ShopApp.exceptions.DataNotFoundException;
import com.example.ShopApp.models.Order;
import com.example.ShopApp.responses.OrderResponse;

import java.util.List;

public interface IOrederService {
    Order creatOrder(OrderDTO orderDTO) throws Exception;

    Order getOrderById(Long id) throws DataNotFoundException;

    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrderById(Long id);

    List<Order> findByUserId(Long userId);
}
