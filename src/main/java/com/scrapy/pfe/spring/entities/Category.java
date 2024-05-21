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
        private String email;


        private boolean approved;


        private boolean posted;


        public boolean isPosted() {
            return posted;
        }

        public void setPosted(boolean posted) {
            this.posted = posted;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isApproved() {
            return approved;
        }

        public void setApproved(boolean approved) {
            this.approved = approved;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public CategoryDto getCategoryDto(){
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(id);
            categoryDto.setName(name);
            categoryDto.setDescription(description);
            categoryDto.setEmail(email);
            return categoryDto;

        }}