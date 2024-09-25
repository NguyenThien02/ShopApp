package com.example.ShopApp.services;

import com.example.ShopApp.dtos.OrderDTO;
import com.example.ShopApp.exceptions.DataNotFoundException;
import com.example.ShopApp.models.Order;
import com.example.ShopApp.models.OrderStatus;
import com.example.ShopApp.models.User;
import com.example.ShopApp.repositories.OrderRepository;
import com.example.ShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrederService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    //Tạo mới một order
    @Override
    public Order creatOrder(OrderDTO orderDTO) throws Exception {
        // Tìm xem user id có tồn tại hay không
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user id = " + orderDTO.getUserId()));
        //Convert OrderDTO --> Order
        //Dùng thư viện Model Mapper
        //Tạo một luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        //Cập nhật các trường của đơn hàng từ orderDTO
        Order order = new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        // Kiểm tra shipping date phải >= ngày hôm này
        LocalDate shippingDate = orderDTO.getShippingDate() == null
                ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today !");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return order;
    }

    // Lấy ra danh sách chi tiết một Order có Id = ?
    @Override
    public Order getOrderById(Long id) throws DataNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("cannot find order with id: " + id));
    }

    // cập nhật đơn hàng order
    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + id));
        User existingUser = userRepository.findById(existingOrder.getUser().getId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: "
                        + existingOrder.getUser().getId()));
        //Tạo một luồng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cập nhật các trường của đơn hàng từ OrderDTO
        modelMapper.map(orderDTO,existingOrder);
        orderRepository.save(existingOrder);
        return existingOrder;
    }
    //Xóa mềm đơn hàng có id =? (Thay active = false)
    @Override
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order != null){
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    // Lấy ra danh sách các order của một User có id = ?
    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
