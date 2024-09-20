package com.example.ShopApp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Controller;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Không có bản ghi nào giống nhau, tự tăng lên 1
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
