package com.example.ShopApp.Controllers;

import com.example.ShopApp.dtos.ProductDTO;
import com.example.ShopApp.dtos.ProductImageDTO;
import com.example.ShopApp.exceptions.DataNotFoundException;
import com.example.ShopApp.models.Category;
import com.example.ShopApp.models.Product;
import com.example.ShopApp.models.ProductImage;
import com.example.ShopApp.responses.ProductListResponse;
import com.example.ShopApp.responses.ProductResponse;
import com.example.ShopApp.services.ProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor

public class ProductController {
    private final ProductService productService;

    //Thêm mới product
    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //Upload file anh product
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImagesProduct(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files){
        try{
            if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("You can only upload maximum " +
                        ProductImage.MAXIMUM_IMAGES_PER_PRODUCT + " image");
            }
            Product existingProduct = productService.getProductById(productId);
            List<ProductImage> productImages = new ArrayList<>();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            for (MultipartFile file : files) {
                if(file.getSize() == 0){
                    continue;
                }
                //Kiểm tra kích thước file và định dạng file
                if (file.getSize() > 10 * 1024 * 1024) {// > 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).
                            body("File is too large! Maximum size is 10MB");
                }
                // Kiểm tra định dạng file
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                //Lưu file và cập nhật thumbnail trong DTO
                String filename = storeFile(file);
                //Tạo ảnh lưu vào data base

                ProductImage productImage  = productService.createProductImage(existingProduct.getId(),
                        ProductImageDTO.builder()
                                .productId(existingProduct.getId())
                                .imageUrl(filename)
                                .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    //Lấy ra ảnh product
    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable("imageName") String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            //Tạo một đối tượng UrlResource từ đường dẫn hình ảnh, cho phép tải tệp hình ảnh từ hệ thống tệp của server.
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        // hình ảnh sẽ được trả về dưới định dạng JPEG.
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        //Nếu hình ảnh không tồn tại, hệ thống sẽ trả về một hình ảnh mặc định
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Lưu file
    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        //Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        //Kiểm tra và tạo thư mục nếu không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        //Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    //Lấy ra danh sách product theo trang
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit

    ) {
        //Tạo ra pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by("id").ascending()
        );
        Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest);
        // Lẩy ra tổng số trang
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                        .build());
    }
    //http://localhost:8088/api/v1/products/6
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") Long productId
    ) {
        try {
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // Xóa Product với id =?
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        productService.deteleProduct(productId);
        return ResponseEntity.ok(String.format("Product deleted with id %d successfully", productId));
    }
    //Xóa toàn bộ product
    @DeleteMapping("/deletes")
    public ResponseEntity<String> deleteAllProducts(){
        productService.deleteAllProducts();
        return ResponseEntity.ok("Delete all product successfully");
    }

    //Update product vơi id = ?
    @PutMapping("{id}")
    public ResponseEntity<?> updateProductById(@PathVariable("id") Long id,
                                               @RequestBody ProductDTO productDTO){
        try {
            productService.updateProduct(id,productDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Update Product with id = " + id + "successfully");
    }

    //Faker
//    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for(int i=0; i<100; i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10,90000000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(1,6))
                    .build();
            if(!productService.existsByCategoryId(productDTO.getCategoryId())){
                continue;
            }
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake Products created successfully");
    }

    // products/by-ids?ids=1,3,5,7
    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids) {
        //eg: 1,3,5,7
        try {
            // Tách chuỗi ids thành một mảng các số nguyên
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Product> products = productService.findProductsByIds(productIds);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
