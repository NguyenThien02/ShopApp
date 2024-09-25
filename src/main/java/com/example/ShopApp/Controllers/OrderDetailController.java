package com.example.ShopApp.Controllers;

import com.example.ShopApp.dtos.OrderDetailDTO;
import com.example.ShopApp.exceptions.DataNotFoundException;
import com.example.ShopApp.models.OrderDetail;
import com.example.ShopApp.responses.OrderDetailResponse;
import com.example.ShopApp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    // Thêm mới 1 order detail
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO,
                BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessager = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessager);
            }
            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Lấy ra order detail với id = ?
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(orderDetail);
    }

    // Lấy ra danh sách các order detail thuộc về order có id = ?
    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("order_id") Long orderId){
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    // Cập nhật Order Detail có id = ?
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable Long id,
            @RequestBody OrderDetailDTO orderDetailDTO
    ) throws DataNotFoundException {
        OrderDetail orderDetail =  orderDetailService.updateOrderDetail(id,orderDetailDTO);
        return ResponseEntity.ok(orderDetail);
    }

    //Xóa cứng Order Detail có id = ?
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
        @Valid @PathVariable Long id
    ){
        orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok("delete order detail successfully");
    }
}
