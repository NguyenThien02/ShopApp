package com.example.ShopApp.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.XSlf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone_number",nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "address",length = 200)
    private String address;

    @Column(name = "note", length = 100)
    private String note;

    @Column(name = "total_money")
    private Float totalMoney;

    @Column(name = "shipping_method", length = 100)
    private String shippingMethod;

    @Column(name = "shipping_address", length = 200)
    private String shippingAddress;

    @Column(name = "payment_method", length = 100)
    private String paymentMethod;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "status")
    private String status;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "tracking_number",length = 100)
    private String trackingNumber;

    @Column(name = "active")
    private Boolean active;

}
