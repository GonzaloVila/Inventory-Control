package com.example.inventorycontrol.controller.web;

import com.example.inventorycontrol.model.Product;
import com.example.inventorycontrol.model.Category;
import com.example.inventorycontrol.model.Provider;
import com.example.inventorycontrol.service.ProductService;
import com.example.inventorycontrol.service.CategoryService;
import com.example.inventorycontrol.service.ProviderService;
import com.example.inventorycontrol.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/web/products")
public class ProductWebController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProviderService providerService;

    private void loadFormAttributes(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("providers", providerService.getAllProviders());
    }

    // 1. Mostrar todos los productos (solo los activos por el @Where
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("pageTitle", "Lista de Productos");
        model.addAttribute("contentFragment", "products/productListContent");
        return "layouts/main";
    }

    // 2. Mostrar formulario para añadir un nuevo producto
    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", new Product());
        }
        loadFormAttributes(model); // Carga categorías y proveedores
        model.addAttribute("pageTitle", "Añadir Producto");
        model.addAttribute("contentFragment", "products/productFormContent");
        return "layouts/main";
    }

    // 3. Procesar el formulario de añadir producto
    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.product", bindingResult);
            redirectAttributes.addFlashAttribute("product", product);
            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            return "redirect:/web/products/add";
        }
        try {
            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Producto añadido exitosamente!");
            return "redirect:/web/products";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("product", product);
            return "redirect:/web/products/add";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al añadir producto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("product", product);
            return "redirect:/web/products/add";
        }
    }

    // 4. Mostrar formulario para editar un producto existente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Producto no encontrado o no activo.");
            return "redirect:/web/products";
        }
        model.addAttribute("product", productOptional.get());
        loadFormAttributes(model);
        model.addAttribute("pageTitle", "Editar Producto");
        model.addAttribute("contentFragment", "products/productFormContent");
        return "layouts/main";
    }

    // 5. Procesar el formulario de editar producto
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.product", bindingResult);
            redirectAttributes.addFlashAttribute("product", product);
            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            return "redirect:/web/products/edit/" + id;
        }
        try {
            product.setId(id);
            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("successMessage", "Producto actualizado exitosamente!");
            return "redirect:/web/products";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("product", product);
            return "redirect:/web/products/edit/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al actualizar producto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("product", product);
            return "redirect:/web/products/edit/" + id;
        }
    }

    // 6. Eliminar lógicamente un producto (soft delete)
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id); // Llama al soft delete
            redirectAttributes.addFlashAttribute("successMessage", "Producto eliminado (lógicamente) exitosamente!");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al eliminar producto: " + e.getMessage());
        }
        return "redirect:/web/products";
    }

    // 7. Mostrar detalles de un producto
    @GetMapping("/{id}")
    public String viewProductDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Producto no encontrado o no activo.");
            return "redirect:/web/products";
        }
        model.addAttribute("product", productOptional.get());
        model.addAttribute("pageTitle", "Detalles del Producto");
        model.addAttribute("contentFragment", "products/productDetailContent");
        return "layouts/main";
    }
}