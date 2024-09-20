package com.example.ShopApp.repositories;

import com.example.ShopApp.models.Category;
import com.example.ShopApp.models.OrderDetail;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    List<OrderDetail> findByOrderId(Long orderId);
}
