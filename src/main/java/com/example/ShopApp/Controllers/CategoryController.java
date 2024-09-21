package com.example.ShopApp.Controllers;

import com.example.ShopApp.dtos.CategoryDTO;
import com.example.ShopApp.models.Category;
import com.example.ShopApp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor

public class CategoryController {
    private final CategoryService categoryService;
    // Tạo mới một category
    @PostMapping("")
    public ResponseEntity<?> insertCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                            BindingResult result) {
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Creat category seccessfully");
    }
    //Hiển thị category có id = ?
    @GetMapping("/{id}")
    public Category findByIdCategory(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    //Hiện tất cả các Categories
    @GetMapping("")//http://localhost:8080/api/v1/categories?page=1&limit=10
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    //Cập nhật category
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable long id,
                                                 @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("Update category successfully");
    }

    //xóa category
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with id = " + id);
    }
}