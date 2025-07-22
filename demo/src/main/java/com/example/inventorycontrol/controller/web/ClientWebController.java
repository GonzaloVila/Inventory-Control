package com.example.inventorycontrol.controller.web;

import com.example.inventorycontrol.model.Client;
import com.example.inventorycontrol.service.ClientService;
import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.exception.DuplicateResourceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/web/clients")
public class ClientWebController {

    @Autowired
    private ClientService clientService;

    // 1. Mostrar todos los clientes
    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("pageTitle", "Lista de Clientes");
        model.addAttribute("contentFragment", "clients/clientListContent"); // <--- AÑADIDO: Especifica el fragmento
        return "layouts/main";
    }

    // 2. Mostrar formulario para añadir un nuevo cliente
    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("client")) {
            model.addAttribute("client", new Client());
        }
        model.addAttribute("pageTitle", "Añadir Cliente");
        model.addAttribute("contentFragment", "clients/clientFormContent"); // <--- AÑADIDO: Especifica el fragmento
        return "layouts/main";
    }

    // 3. Procesar el formulario de añadir cliente
    @PostMapping("/add")
    public String addClient(@Valid @ModelAttribute("client") Client client,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.client", bindingResult);
            redirectAttributes.addFlashAttribute("client", client);
            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            // Cuando hay errores, es una redirección, por lo que el fragmento se manejará en el GET /add
            return "redirect:/web/clients/add"; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        }
        try {
            clientService.createClient(client);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente añadido exitosamente!");
            return "redirect:/web/clients"; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        } catch (DuplicateResourceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("client", client);
            return "redirect:/web/clients/add"; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al añadir cliente: " + e.getMessage());
            redirectAttributes.addFlashAttribute("client", client);
            return "redirect:/web/clients/add"; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        }
    }

    // 4. Mostrar formulario para editar un cliente existente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Client> clientOptional = clientService.getClientById(id);
        if (clientOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cliente no encontrado.");
            return "redirect:/web/clients"; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        }
        model.addAttribute("client", clientOptional.get());
        model.addAttribute("pageTitle", "Editar Cliente");
        model.addAttribute("contentFragment", "clients/clientFormContent"); // <--- AÑADIDO: Especifica el fragmento
        return "layouts/main";
    }

    // 5. Procesar el formulario de editar cliente
    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable Long id,
                               @Valid @ModelAttribute("client") Client client,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.client", bindingResult);
            redirectAttributes.addFlashAttribute("client", client);
            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            // Cuando hay errores, es una redirección al formulario de edición
            return "redirect:/web/clients/edit/" + id; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        }
        try {
            client.setId(id);
            clientService.updateClient(client);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente actualizado exitosamente!");
            return "redirect:/web/clients"; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("client", client);
            return "redirect:/web/clients/edit/" + id; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        } catch (DuplicateResourceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("client", client);
            return "redirect:/web/clients/edit/" + id; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al actualizar cliente: " + e.getMessage());
            redirectAttributes.addFlashAttribute("client", client);
            return "redirect:/web/clients/edit/" + id; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        }
    }

    // 6. Eliminar un cliente
    @PostMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clientService.deleteClient(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente eliminado exitosamente!");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al eliminar cliente: " + e.getMessage());
        }
        return "redirect:/web/clients"; // <--- CAMBIO: Debe ser una REDIRECCIÓN
    }

    // 7. Mostrar detalles de un cliente
    @GetMapping("/{id}")
    public String viewClientDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Client> clientOptional = clientService.getClientById(id);
        if (clientOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cliente no encontrado.");
            return "redirect:/web/clients"; // <--- CAMBIO: Debe ser una REDIRECCIÓN
        }
        model.addAttribute("client", clientOptional.get());
        model.addAttribute("pageTitle", "Detalles del Cliente");
        model.addAttribute("contentFragment", "clients/clientDetailContent"); // <--- AÑADIDO: Especifica el fragmento
        return "layouts/main";
    }
}