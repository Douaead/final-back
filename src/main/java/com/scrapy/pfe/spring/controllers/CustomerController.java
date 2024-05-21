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

public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

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
        if (categoryDtoList == null) return ResponseEntity.notFound().build();
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
    @PutMapping("/categories/{id}/reapprove-repost")
    public ResponseEntity<CategoryDto> reapproveAndRepostCategory(@PathVariable Long id) {
        try {
            // Réapprouver et Reposter la catégorie avec l'identifiant spécifié
            CategoryDto reapprovedCategoryDto = customerService.reapproveAndRepostCategory(id);
            if (reapprovedCategoryDto != null) {
                return ResponseEntity.ok(reapprovedCategoryDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/categories/{id}/approve")
    public ResponseEntity<CategoryDto> approveAndSendCategory(@PathVariable Long id) {
        try {
            // Approuver la catégorie avec l'identifiant spécifié
            CategoryDto approvedCategoryDto = customerService.approveCategory(id);
            if (approvedCategoryDto != null) {
                // Envoyer la catégorie approuvée
                approvedCategoryDto = customerService.sendCategory(id);
                return ResponseEntity.ok(approvedCategoryDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/categories/{id}/send")
    public ResponseEntity<CategoryDto> sendCategory(@PathVariable Long id) {
        try {
            // Récupérer la catégorie approuvée avec l'identifiant spécifié
            CategoryDto approvedCategoryDto = customerService.getApprovedCategory(id);
            if (approvedCategoryDto != null) {
                // Envoyer la catégorie approuvée à une autre forme (ou à une autre API)
                // Vous pouvez implémenter la logique pour envoyer la catégorie où vous en avez besoin
                return ResponseEntity.ok(approvedCategoryDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/approved-and-posted-categories")
    public ResponseEntity<List<CategoryDto>> getApprovedAndPostedCategories() {
        List<CategoryDto> approvedAndPostedCategories = customerService.getApprovedAndPostedCategories();
        return ResponseEntity.ok(approvedAndPostedCategories);
    }



}