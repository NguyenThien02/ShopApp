package com.example.ShopApp.repositories;

import com.example.ShopApp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name); // Xem sản phẩm có tên đó có tồn tại hay không

    boolean existsByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Product> searchProducts
    (@Param("categoryId") Long categoryId,
     @Param("keyword") String keyword, Pageable pageable);
}
