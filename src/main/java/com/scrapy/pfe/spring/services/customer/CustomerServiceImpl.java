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
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        if (categoryDto.getImg() != null) {
            category.setImg(categoryDto.getImg());
        }
        // Save category to repository
        Category createdCategory = categoryRepository.save(category);

        // Create CategoryDto with ID, Name, Description, and Image
        CategoryDto createdCategoryDto = new CategoryDto();
        createdCategoryDto.setId(createdCategory.getId());
        createdCategoryDto.setName(createdCategory.getName());
        createdCategoryDto.setDescription(createdCategory.getDescription());
        createdCategoryDto.setImg(createdCategory.getImg());
        return createdCategoryDto;
    }
}