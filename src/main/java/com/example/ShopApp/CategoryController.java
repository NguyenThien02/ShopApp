package com.example.ShopApp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/categories")

public class CategoryController {
    //Hiện tất cả các Categories
    @GetMapping("")
    public ResponseEntity<String> getAllCategories(){
        return ResponseEntity.ok("Show all categories");
    }

    @PostMapping("")
    public ResponseEntity<String> insertCategory(){
        return  ResponseEntity.ok("This is insert category");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable long id){
        return ResponseEntity.ok("Insert category with id = " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
        return ResponseEntity.ok("Delete category with id = " + id);
    }
}
