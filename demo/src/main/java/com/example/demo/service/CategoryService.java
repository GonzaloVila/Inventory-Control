package com.example.demo.service;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {


    @Autowired // Inyección de dependencia para acceder al repositorio
    CategoryRepository categoryRepository;

    // Obtener todas las categorias
    public List<Category> getAllCategorys(){
        return categoryRepository.findAll();
    }

    // Crear una categoria
    public Category createCategory(Category category){
        return categoryRepository.save(category);
    }

    // Obtener una categoria por ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Actualiza una categoria existente
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Eliminar una categoria
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
