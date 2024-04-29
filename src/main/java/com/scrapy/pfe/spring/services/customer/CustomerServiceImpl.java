package com.scrapy.pfe.spring.services.customer;

import com.scrapy.pfe.spring.dtos.CategoryDto;
import com.scrapy.pfe.spring.entities.Category;
import com.scrapy.pfe.spring.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto postCategory(CategoryDto categoryDto) throws IOException {
        // Création de l'entité Category à partir du DTO reçu
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        // Sauvegarde de l'entité Category dans la base de données
        Category createdCategory = categoryRepository.save(category);

        // Création d'un nouveau DTO pour la réponse, avec tous les champs nécessaires
        CategoryDto createdCategoryDto = new CategoryDto();
        createdCategoryDto.setId(createdCategory.getId());
        createdCategoryDto.setName(createdCategory.getName());
        createdCategoryDto.setDescription(createdCategory.getDescription());



        return createdCategoryDto;
    }
}