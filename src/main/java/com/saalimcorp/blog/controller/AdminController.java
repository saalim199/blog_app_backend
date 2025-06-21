package com.saalimcorp.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saalimcorp.blog.entity.Category;
import com.saalimcorp.blog.entity.User;
import com.saalimcorp.blog.service.CategoryService;
import com.saalimcorp.blog.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users != null) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>("No users found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        var createdCategory = categoryService.createCategory(category);
        if (createdCategory != null) {
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Category creation failed", HttpStatus.BAD_REQUEST);
    }
}
