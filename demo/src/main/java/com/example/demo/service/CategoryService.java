package com.example.demo.service;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
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
        if (categoryRepository.existsByName(category.getName())) {
            throw new DuplicateResourceException("Ya existe una categoría con el nombre: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    // Obtener una categoria por ID
    public Optional<Category> getCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría con ID " + id + " no encontrada.");
        }
        return categoryRepository.findById(id);
    }

    // Actualiza una categoria existente
    public Category updateCategory(Category category) {
        if (!categoryRepository.existsById(category.getId())) {
            throw new ResourceNotFoundException("No se puede actualizar: categoría con ID " + category.getId() + " no existe.");
        }
        return categoryRepository.save(category);
    }

    // Eliminar una categoria
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: categoría con ID " + id + " no existe.");
        }
        categoryRepository.deleteById(id);
    }
}
