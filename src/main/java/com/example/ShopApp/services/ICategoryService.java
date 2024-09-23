package com.example.ShopApp.services;

import com.example.ShopApp.dtos.CategoryDTO;
import com.example.ShopApp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();

    Category updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    boolean exsitingById(Long id);
}