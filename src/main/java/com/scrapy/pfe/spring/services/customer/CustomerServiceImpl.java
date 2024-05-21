package com.scrapy.pfe.spring.services.customer;

import com.scrapy.pfe.spring.dtos.CategoryDto;
import com.scrapy.pfe.spring.entities.Category;
import com.scrapy.pfe.spring.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CustomerServiceImpl implements CustomerService {
    private final CategoryRepository categoryRepository;

    public CustomerServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public CategoryDto postCategory(CategoryDto categoryDto) throws IOException {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        Category createdCategory = categoryRepository.save(category);
        return createdCategory.getCategoryDto();
    }

    @Override
    public List<CategoryDto> getAllCategoriesByTitle(String title) {
        return categoryRepository.findAllByNameContaining(title).stream().map(Category::getCategoryDto).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(Category::getCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto approveCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            // Mettre à jour l'état de la catégorie pour l'approuver
            category.setApproved(true);
            categoryRepository.save(category); // Enregistrer les modifications dans la base de données

            // Créer un objet CategoryDto à partir des attributs de Category
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDto.setDescription(category.getDescription());
            categoryDto.setEmail(category.getEmail());
            categoryDto.setApproved(category.isApproved()); // Utiliser isApproved() pour les booléens

            return categoryDto;
        } else {
            // Gérer le cas où la catégorie avec l'ID spécifié n'est pas trouvée
            return null;
        }
    }

    @Override
    public CategoryDto sendCategory(Long id) {
        // Récupérer la catégorie approuvée avec l'identifiant spécifié
        CategoryDto approvedCategoryDto = getApprovedCategory(id);

        // Marquer la catégorie comme postée dans votre système
        approvedCategoryDto.setPosted(true);

        // Mettre à jour la catégorie dans votre base de données
        Category updatedCategory = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        updatedCategory.setPosted(true);
        categoryRepository.save(updatedCategory);

        return approvedCategoryDto;
    }


    @Override
    public CategoryDto reapproveAndRepostCategory(Long id) {
        // Récupérer la catégorie par son ID
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            // Vérifier si la catégorie est déjà approuvée
            if (category.isApproved()) {
                // Marquer la catégorie comme non postée
                category.setPosted(false);

                // Mettre à jour la catégorie dans la base de données
                categoryRepository.save(category);

                // Créer un objet CategoryDto à partir des attributs de Category
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(category.getId());
                categoryDto.setName(category.getName());
                categoryDto.setDescription(category.getDescription());
                categoryDto.setEmail(category.getEmail());
                categoryDto.setApproved(category.isApproved());
                categoryDto.setPosted(category.isPosted());

                // Retourner la catégorie réapprouvée et non postée
                return categoryDto;
            } else {
                // La catégorie n'est pas approuvée, retourner null ou gérer l'erreur selon vos besoins
                return null;
            }
        } else {
            // Gérer le cas où la catégorie avec l'ID spécifié n'est pas trouvée
            return null;
        }
    }


    @Override
    public List<CategoryDto> getPostedCategories() {
        List<Category> postedCategories = categoryRepository.findByPosted(true);
        return mapCategoriesToCategoryDto(postedCategories);
    }

    // Méthode utilitaire pour mapper les catégories à CategoryDto
    private List<CategoryDto> mapCategoriesToCategoryDto(List<Category> categories) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDto.setDescription(category.getDescription());
            categoryDto.setEmail(category.getEmail());
            // Autres attributs à mapper si nécessaire

            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }

    @Override
    public List<CategoryDto> getApprovedAndPostedCategories() {
        List<Category> approvedAndPostedCategories = categoryRepository.findByApprovedTrueAndPostedTrue();
        return convertToCategoryDtoList(approvedAndPostedCategories);
    }

    private List<CategoryDto> convertToCategoryDtoList(List<Category> categories) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();

        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDto.setDescription(category.getDescription());
            categoryDto.setEmail(category.getEmail());
            categoryDto.setApproved(category.isApproved());
            categoryDto.setPosted(category.isPosted());

            categoryDtoList.add(categoryDto);
        }

        return categoryDtoList;
    }


    @Override
    public CategoryDto getApprovedCategory(Long id) {
        // Récupérer la catégorie depuis la source de données
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        // Vérifier si la catégorie existe
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            // Vérifier si la catégorie est approuvée
            if (category.isApproved()) {
                // Mapper les attributs de la catégorie vers un objet CategoryDto
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(category.getId());
                categoryDto.setName(category.getName());
                categoryDto.setDescription(category.getDescription());
                categoryDto.setEmail(category.getEmail());
                // Autres attributs à mapper si nécessaire

                return categoryDto;
            } else {
                // Gérer le cas où la catégorie n'est pas approuvée
                throw new RuntimeException("La catégorie n'est pas approuvée");
            }
        } else {
            // Gérer le cas où la catégorie avec l'ID spécifié n'est pas trouvée
            throw new RuntimeException("Catégorie non trouvée");
        }
    }

}