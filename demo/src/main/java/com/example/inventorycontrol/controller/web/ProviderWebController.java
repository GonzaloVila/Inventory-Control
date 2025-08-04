package com.example.inventorycontrol.controller.web;

import com.example.inventorycontrol.exception.DuplicateResourceException;
import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Provider;
import com.example.inventorycontrol.service.ProviderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/web/providers")
public class ProviderWebController {

    @Autowired
    private ProviderService providerService;

    // 1. Mostrar la lista de proveedores
    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_MANAGER', 'ROL_EMPLOYEE', 'ROL_USER')")
    @GetMapping
    public String listProviders(Model model) {
        model.addAttribute("providers", providerService.getAllProviders());
        model.addAttribute("pageTitle", "Lista de Proveedores");
        model.addAttribute("contentFragment", "providers/providerListContent");
        return "layouts/main";
    }

    // 2. Mostrar formulario para añadir un nuevo proveedor
    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_MANAGER')")
    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("provider")) {
            model.addAttribute("provider", new Provider());
        }
        model.addAttribute("pageTitle", "Añadir Proveedor");
        model.addAttribute("contentFragment", "providers/providerFormContent");
        return "layouts/main";
    }

    // 3. Procesar el formulario de añadir proveedor
    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_MANAGER')")
    @PostMapping("/add")
    public String addProvider(@Valid @ModelAttribute("provider") Provider provider,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.provider", bindingResult);
            redirectAttributes.addFlashAttribute("provider", provider);
            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            return "redirect:/web/providers/add";
        }
        try {
            providerService.createProvider(provider);
            redirectAttributes.addFlashAttribute("successMessage", "Proveedor añadido exitosamente!");
            return "redirect:/web/providers";
        } catch (DuplicateResourceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("provider", provider);
            return "redirect:/web/providers/add";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al añadir proveedor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("provider", provider);
            return "redirect:/web/providers/add";
        }
    }

    // 4. Mostrar formulario para editar un proveedor existente
    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_MANAGER')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Provider> providerOptional = providerService.getProviderById(id);
        if (providerOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Proveedor no encontrado.");
            return "redirect:/web/providers";
        }
        model.addAttribute("provider", providerOptional.get());
        model.addAttribute("pageTitle", "Editar Proveedor");
        model.addAttribute("contentFragment", "providers/providerFormContent");
        return "layouts/main";
    }

    // 5. Procesar el formulario de editar proveedor
    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_MANAGER')")
    @PostMapping("/update/{id}")
    public String updateProvider(@PathVariable Long id,
                                 @Valid @ModelAttribute("provider") Provider provider,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.provider", bindingResult);
            redirectAttributes.addFlashAttribute("provider", provider);
            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            return "redirect:/web/providers/edit/" + id;
        }
        try {
            provider.setId(id);
            providerService.updateProvider(id, provider);
            redirectAttributes.addFlashAttribute("successMessage", "Proveedor actualizado exitosamente!");
            return "redirect:/web/providers";
        } catch (ResourceNotFoundException | DuplicateResourceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("provider", provider);
            return "redirect:/web/providers/edit/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al actualizar proveedor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("provider", provider);
            return "redirect:/web/providers/edit/" + id;
        }
    }

    // 6. Eliminar un proveedor
    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_MANAGER')")
    @PostMapping("/delete/{id}")
    public String deleteProvider(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            providerService.deleteProvider(id);
            redirectAttributes.addFlashAttribute("successMessage", "Proveedor eliminado exitosamente!");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al eliminar proveedor: " + e.getMessage());
        }
        return "redirect:/web/providers";
    }

    // 7. Mostrar detalles de un proveedor
    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_MANAGER', 'ROL_EMPLOYEE')")
    @GetMapping("/{id}")
    public String viewProviderDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Provider> providerOptional = providerService.getProviderById(id);
        if (providerOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Proveedor no encontrado.");
            return "redirect:/web/providers";
        }
        model.addAttribute("provider", providerOptional.get());
        model.addAttribute("pageTitle", "Detalles del Proveedor");
        model.addAttribute("contentFragment", "providers/providerDetailContent");
        return "layouts/main";
    }
}