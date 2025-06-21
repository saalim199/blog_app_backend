package com.saalimcorp.blog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.saalimcorp.blog.entity.Category;
import com.saalimcorp.blog.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        try {
            if (category != null && category.getName() != null && !category.getName().isEmpty()) {
                category.setName(category.getName().trim());
                category.setCreatedAt(LocalDateTime.now());
                categoryRepository.save(category);
                return category;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error creating category: {}", e.getMessage());
            return null;
        }
    }

    public List<Category> getAllCategory() {
        try {
            var categories = categoryRepository.findAll();
            return categories;
        } catch (Exception e) {
            log.error("Error fetching categories: {}", e.getMessage());
            return null;
        }
    }

    public Category getCategoryByName(String name) {
        try {
            return categoryRepository.findByName(name);
        } catch (Exception e) {
            log.error("Error fetching category by name {}: {}", name, e.getMessage());
            return null;
        }
    }
}
