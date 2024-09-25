package com.example.ShopApp.responses;

import com.example.ShopApp.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class OrderResponse {

    private Long id;

    @JoinColumn(name = "user_id")
    private User user;

    private String fullName;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;

    private String note;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    private String status;

    @Column(name = "total_money")
    private Float totalMoney;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_date")
    private Date shippingDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "payment_method")
    private String paymentMethod;

    private Boolean active;

}
