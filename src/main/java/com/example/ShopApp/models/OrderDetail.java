package com.example.ShopApp.models;

import jakarta.persistence.*;
import lombok.*;
import org.aspectj.weaver.ast.Or;

@Entity
@Table(name = "order_details")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price")
    private Float price;

    @Column(name = "number_of_products",nullable = false, length = 11)
    private int numberOfProducts;

    @Column(name = "total_money",nullable = false)
    private Float totalMoney;

    @Column(name = "color", length = 20)
    private String color;
}
