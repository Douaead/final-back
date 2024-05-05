package com.scrapy.pfe.spring.controllers;

import com.scrapy.pfe.spring.dtos.CategoryDto;
import com.scrapy.pfe.spring.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
@GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
    List<CategoryDto> categoryDtoList = customerService.getAllCategories();
    if(categoryDtoList == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(categoryDtoList);
}
    @GetMapping("/categories/{title}")
    public ResponseEntity<List<CategoryDto>> getAllCategoriesByTitle(@PathVariable String title) {
        List<CategoryDto> categoryDtoList = customerService.getAllCategoriesByTitle(title);
        if (categoryDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryDtoList);
    }



}
