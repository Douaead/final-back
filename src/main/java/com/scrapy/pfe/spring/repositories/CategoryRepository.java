package com.scrapy.pfe.spring.repositories;

import com.scrapy.pfe.spring.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository <Category,Long> {
   List<Category> findAllByNameContaining(String title);
}
