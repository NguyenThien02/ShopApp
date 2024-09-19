package com.example.ShopApp.Controllers;

import com.example.ShopApp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
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
            return ResponseEntity.ok("Create order detail successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long id){
        return ResponseEntity.ok("Get order detail with id: " + id);
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("order_id") Long orderId){
        return ResponseEntity.ok("Get All Order detail with order id: " + orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable Long id,
            @RequestBody OrderDetailDTO newOrderDetailData
    ){
        return ResponseEntity.ok("update order details successfully with id: " + id +"\n"+ "newOrderDetailData: " + newOrderDetailData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
        @Valid @PathVariable Long id
    ){
//        return ResponseEntity.ok("Delete order detail scuccessfully with id: " + id);
            return ResponseEntity.noContent().build();
    }
}
