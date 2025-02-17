package com.example.demo.service;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CategoryService {


    @Autowired // Inyección de dependencia para acceder al repositorio
    CategoryRepository categoryRepository;

    // Obtener todas las categorias
    public ArrayList<Category> getAllCategorys(){
        return (ArrayList<Category>) categoryRepository.findAll();
    }

    // Obtener una categoria por ID
    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);  // Retorna null si no se encuentra la categoria
    }

    // Guardar una nueva categoria o actualizar una existente
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Eliminar una categoria
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
