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

    private void prepareProductForm(Product product, Model model) {
        if (product.getCategory() == null) {
            product.setCategory(new Category());
        }
        if (product.getProvider() == null) {
            product.setProvider(new Provider());
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("providers", providerService.getAllProviders());
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("pageTitle", "Lista de Productos");
        model.addAttribute("contentFragment", "products/productListContent :: productListContent");
        return "layouts/main";
    }

    // 2. Mostrar formulario para añadir un nuevo producto
    @GetMapping("/add")
    public String showAddForm(Model model) {
        Product product = new Product();
        prepareProductForm(product, model);
        model.addAttribute("pageTitle", "Añadir Producto (TEST)");
        model.addAttribute("contentFragment", "products/productFormContent :: productFormContent");
        return "layouts/main";
    }

    // 3. Procesar el formulario de añadir producto
    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            prepareProductForm(product, model);
            model.addAttribute("pageTitle", product.getId() == null ? "Añadir Producto (TEST)" : "Editar Producto (TEST)");
            model.addAttribute("contentFragment", "products/productFormContent :: productFormContent");
            return "layouts/main";
        }

        // Si la validación es exitosa, guardar el producto
        if (product.getCategory().getId() == null) {
            product.setCategory(null);
        }
        if (product.getProvider().getId() == null) {
            product.setProvider(null);
        }

        productService.createProduct(product);
        redirectAttributes.addFlashAttribute("message", "Producto guardado con éxito.");
        return "redirect:/products";
    }

    // 4. Mostrar formulario para editar un producto existente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));

        prepareProductForm(product, model);

        model.addAttribute("pageTitle", "Editar Producto (TEST)");
        model.addAttribute("contentFragment", "products/productFormContent :: productFormContent");
        return "layouts/main";
    }

    // 5. Procesar el formulario de editar producto
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        // Validar si la categoría y el proveedor no son nulos
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            bindingResult.rejectValue("category", "NotNull.product.category", "Debes seleccionar una categoría.");
        }
        if (product.getProvider() == null || product.getProvider().getId() == null) {
            bindingResult.rejectValue("provider", "NotNull.product.provider", "Debes seleccionar un proveedor.");
        }

        if (bindingResult.hasErrors()) {
            product.setId(id);
            model.addAttribute("product", product); // Asegúrate de pasar el objeto con los errores de vuelta al formulario
            model.addAttribute("pageTitle", "Editar Producto (TEST)");
            // Llama a un método para preparar el formulario (cargar categorías y proveedores)
            // Puedes refactorizar el código de `prepareProductForm` en un método privado
            // para evitar duplicación.
            // Aquí puedes llamar a los servicios para obtener las listas
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("providers", providerService.getAllProviders());
            model.addAttribute("contentFragment", "products/productFormContent :: productFormContent");
            return "layouts/main";
        }

        try {
            // En este punto ya tienes un id de categoría y proveedor, por lo que no es necesario
            // verificar si son nulos. Solo necesitas actualizar el producto.
            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("successMessage", "Producto actualizado exitosamente!");
            return "redirect:/web/products";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/web/products/edit/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al actualizar producto: " + e.getMessage());
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
        model.addAttribute("contentFragment", "products/productDetailContent :: productDetailContent");
        return "layouts/main";
    }
}