package com.example.ShopApp.Controllers;

import com.example.ShopApp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")


public class CategoryController {
    //Hiện tất cả các Categories
    @GetMapping("")//http://localhost:8080/api/v1/categories?page=1&limit=10
    public ResponseEntity<String> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok(String.format("GetAllCategory page %d, limit %d", page,limit));
    }

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
        return ResponseEntity.ok("This is insert category" + categoryDTO );
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable long id) {
        return ResponseEntity.ok("Insert category with id = " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id) {
        return ResponseEntity.ok("Delete category with id = " + id);
    }
}
