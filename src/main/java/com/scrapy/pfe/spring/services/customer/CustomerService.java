package com.scrapy.pfe.spring.services.customer;

import com.scrapy.pfe.spring.dtos.CategoryDto;

import java.io.IOException;

public interface CustomerService {
    CategoryDto postCategory(CategoryDto categoryDto) throws IOException;
}
