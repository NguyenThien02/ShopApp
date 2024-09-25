package com.example.ShopApp.services;

import com.example.ShopApp.dtos.OrderDetailDTO;
import com.example.ShopApp.exceptions.DataNotFoundException;
import com.example.ShopApp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;

    List<OrderDetail> findByOrderId(Long orderId);

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    void deleteOrderDetail(Long id);


}
