package com.saalimcorp.blog.controller;

import com.saalimcorp.blog.dto.ResponseDTO;
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
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<Category> categories = categoryService.getAllCategory();
                responseDTO.setMessage("Success");
                responseDTO.setData(categories);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }catch (Exception e) {
            responseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
