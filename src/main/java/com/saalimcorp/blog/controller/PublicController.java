package com.saalimcorp.blog.controller;

import com.saalimcorp.blog.entity.Category;
import com.saalimcorp.blog.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/public")
@AllArgsConstructor
public class PublicController {
    private final CategoryService categoryService;
    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }

    @GetMapping("/all-categories")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategory();
        if (categories != null) {
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
        return new ResponseEntity<>("No categories found", HttpStatus.NOT_FOUND);
    }
}
