package com.scrapy.pfe.spring.entities;

import com.scrapy.pfe.spring.dtos.CategoryDto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

public CategoryDto getCategoryDto(){
CategoryDto categoryDto = new CategoryDto();

categoryDto.setId(id);
categoryDto.setName(name);
categoryDto.setDescription(description);
return categoryDto;
}


}
