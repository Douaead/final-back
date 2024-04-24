package com.scrapy.pfe.spring.controllers;


import com.scrapy.pfe.spring.dtos.CategoryDto;
import com.scrapy.pfe.spring.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
    public class CustomerController {
        private final CustomerService customerService;

        @PostMapping("/category")
        public ResponseEntity<CategoryDto> postCategory(@RequestBody CategoryDto categoryDto) {
            try {
                CategoryDto createdCategoryDto = customerService.postCategory(categoryDto);
                if (createdCategoryDto != null) {
                    return ResponseEntity.ok(createdCategoryDto);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (IOException e) {
                // Gérer les erreurs de traitement de la catégorie
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

