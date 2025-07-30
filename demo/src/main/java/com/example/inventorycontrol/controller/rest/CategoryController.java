package com.example.inventorycontrol.controller.rest;

import com.example.inventorycontrol.model.Category;
import com.example.inventorycontrol.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    // Obtener todas las categorias
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategorys() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // Obtener una categoria por ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id).orElse(null);
        return ResponseEntity.ok(category);
    }

    // Crear una nueva categoria
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    // Actualizar una categoria existente
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory( @RequestBody Category category) {
        ResponseEntity<Category> response = null;
        if(category.getId() != null && categoryService.getCategoryById(category.getId()).isPresent()){
            response = ResponseEntity.ok(categoryService.updateCategory(category));
        }else{
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return  response;
    }

    // Eliminar una categoria
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        ResponseEntity<String> response = null;
        if (categoryService.getCategoryById(id).isPresent()){
            categoryService.deleteCategory(id);
            response = ResponseEntity.status(HttpStatus.NO_CONTENT).body("Category delete");
        }else{
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return response;
    }
}
