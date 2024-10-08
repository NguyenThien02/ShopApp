package com.example.ShopApp.services;

import com.example.ShopApp.dtos.ProductDTO;
import com.example.ShopApp.dtos.ProductImageDTO;
import com.example.ShopApp.exceptions.DataNotFoundException;
import com.example.ShopApp.models.Product;
import com.example.ShopApp.models.ProductImage;
import com.example.ShopApp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(Long id) throws Exception;

    Page<ProductResponse> getAllProducts(String keyword,
                                         Long categoryId, PageRequest pageRequest);

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deteleProduct(Long id);

    void deleteAllProducts();

    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;

    List<ProductImage> allImagesProductById(Long productId);

    boolean existsByCategoryId(Long categoryId);
}
