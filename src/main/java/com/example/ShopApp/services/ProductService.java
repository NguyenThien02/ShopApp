package com.example.ShopApp.services;

import com.example.ShopApp.dtos.ProductDTO;
import com.example.ShopApp.dtos.ProductImageDTO;
import com.example.ShopApp.exceptions.DataNotFoundException;
import com.example.ShopApp.exceptions.InvalidParamException;
import com.example.ShopApp.models.Category;
import com.example.ShopApp.models.Product;
import com.example.ShopApp.models.ProductImage;
import com.example.ShopApp.repositories.CategoryRepository;
import com.example.ShopApp.repositories.ProductImageRepository;
import com.example.ShopApp.repositories.ProductRepository;
import com.example.ShopApp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    //Tạo ra product mới
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        //Kiểm tra xem có Category ID trong bảng categorys ko
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find category with id: " + productDTO.getCategoryId() ));
        //Tạo ra product mới
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .thumbnail(productDTO.getThumbnail())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    //Lấy ra một product có Id=?
    @Override
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + id));
    }
    //Lấy danh sách tất cả các Products theo Page và Limit
    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(product -> {
            ProductResponse productResponse = ProductResponse.builder()
                    .name(product.getName())
                    .price(product.getPrice())
                    .thumbnail(product.getThumbnail())
                    .description(product.getDescription())
                    .categoryId(product.getCategory().getId())
                    .build();
            productResponse.setCreatedAt(product.getCreatedAt());
            productResponse.setUpdatedAt(product.getUpdatedAt());
            return productResponse;
        });
    }

    //Cập nhật Product
    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        //Kiểm tra xem category trong prodcutDTO có trong bảng categorys không
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("cannot find category with id: " + productDTO.getCategoryId()));
        existingProduct.setName(productDTO.getName());
        existingProduct.setCategory(existingCategory);
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        return productRepository.save(existingProduct);
    }

    //kiểm tra xem product có name=? hay không
    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    //Kiểm tra xem đã có categoryId này trong bảng category khong
    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    //Thêm ảnh cho product
    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO)
            throws Exception {
        // Kiểm tra xem product của productImage có trong bảng prduct không
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + productImageDTO.getProductId()));

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        //Không cho insert quá 5 ảnh trong 1 sản phẩm
        int size = productImageRepository.findByProductId(newProductImage.getId()).size();
        if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParamException("Number of images must be <= " +
                    ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }

    // Lấy tất cả các ảnh có productId=?
    @Override
    public List<ProductImage> allImagesProductById(Long productId) {
        List<ProductImage> productImages = productImageRepository.findByProductId(productId);
        return productImages;
    }

    //Xóa product có id=?
    @Override
    public void deteleProduct(Long id) {
//        productRepository.deleteById(id);
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

}
