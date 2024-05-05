package com.scrapy.pfe.spring.services.customer;

import com.scrapy.pfe.spring.dtos.CategoryDto;

import java.io.IOException;
import java.util.List;

public interface CustomerService {
    CategoryDto postCategory(CategoryDto categoryDto) throws IOException;

    List<CategoryDto> getAllCategories();

    List<CategoryDto> getAllCategoriesByTitle(String title);
}
