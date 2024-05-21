package com.scrapy.pfe.spring.services.customer;

import com.scrapy.pfe.spring.dtos.CategoryDto;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    CategoryDto postCategory(CategoryDto categoryDto) throws IOException;
    List<CategoryDto> getAllCategories() ;

    CategoryDto reapproveAndRepostCategory(Long id) ;

    List<CategoryDto> getApprovedAndPostedCategories();

    CategoryDto getApprovedCategory(Long id);
    CategoryDto sendCategory(Long id);


    List<CategoryDto> getAllCategoriesByTitle(String title);
    CategoryDto approveCategory(Long id);

    List<CategoryDto> getPostedCategories();




}
