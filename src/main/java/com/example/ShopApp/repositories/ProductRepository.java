package com.example.ShopApp.repositories;

import com.example.ShopApp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
//    boolean existByName(String name); // Xem sản phẩm có tên đó có tồn tại hay không

//    @Override
//    Page<Product> findAll(Pageable pageable); // Phân trang
}