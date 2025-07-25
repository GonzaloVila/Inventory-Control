package com.example.inventorycontrol.controller.web;

import com.example.inventorycontrol.model.CustomerOrder;
import com.example.inventorycontrol.model.Client; // Necesitarás Client para el formulario
import com.example.inventorycontrol.model.Employee; // Necesitarás Employee para el formulario
import com.example.inventorycontrol.model.Provider; // Necesitarás Provider para el formulario
import com.example.inventorycontrol.service.CustomerOrderService;
import com.example.inventorycontrol.service.ClientService; // Servicio para obtener clientes
import com.example.inventorycontrol.service.EmployeeService; // Servicio para obtener empleados
import com.example.inventorycontrol.service.ProviderService; // Servicio para obtener proveedores
import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.exception.DuplicateResourceException; // Si manejas duplicados en órdenes

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
@RequestMapping("/web/orders")
public class CustomerOrderWebController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProviderService providerService;

    // Metodo auxiliar para cargar datos comunes en los formularios
    private void loadFormAttributes(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("providers", providerService.getAllProviders());
        model.addAttribute("orderStates", List.of("PENDING", "COMPLETED", "SHIPPED", "CANCELED"));
    }

    // 1. Mostrar todas las órdenes
    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", customerOrderService.getAllOrders());
        model.addAttribute("pageTitle", "Lista de Pedidos");
        model.addAttribute("contentFragment", "orders/orderListContent");
        return "layouts/main";
    }

    // 2. Mostrar formulario para añadir una nueva orden
    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("order")) {
            model.addAttribute("order", new CustomerOrder());
        }
        loadFormAttributes(model); // Carga clientes, empleados y proveedores
        model.addAttribute("pageTitle", "Añadir Pedido");
        model.addAttribute("contentFragment", "orders/orderFormContent");
        return "layouts/main";
    }

    // 3. Procesar el formulario de añadir orden
    @PostMapping("/add")
    public String addOrder(@Valid @ModelAttribute("order") CustomerOrder order,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) { // Agrega Model para el caso de errores
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.order", bindingResult);
            redirectAttributes.addFlashAttribute("order", order);
            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            return "redirect:/web/orders/add"; // Redirecciona de vuelta al formulario con errores
        }
        try {
            customerOrderService.createCustomerOrder(order);
            redirectAttributes.addFlashAttribute("successMessage", "Pedido añadido exitosamente!");
            return "redirect:/web/orders";
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("order", order);
            return "redirect:/web/orders/add";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al añadir pedido: " + e.getMessage());
            redirectAttributes.addFlashAttribute("order", order);
            return "redirect:/web/orders/add";
        }
    }

    // 4. Mostrar formulario para editar una orden existente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CustomerOrder> orderOptional = customerOrderService.getOrderById(id);
        if (orderOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Pedido no encontrado.");
            return "redirect:/web/orders";
        }
        model.addAttribute("order", orderOptional.get());
        loadFormAttributes(model); // Carga clientes, empleados y proveedores
        model.addAttribute("pageTitle", "Editar Pedido");
        model.addAttribute("contentFragment", "orders/orderFormContent");
        return "layouts/main";
    }

    // 5. Procesar el formulario de editar orden
    @PostMapping("/update/{id}")
    public String updateOrder(@PathVariable Long id,
                              @Valid @ModelAttribute("order") CustomerOrder order,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) { // Agrega Model para el caso de errores
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.order", bindingResult);
            redirectAttributes.addFlashAttribute("order", order);
            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            return "redirect:/web/orders/edit/" + id; // Redirecciona de vuelta al formulario de edición con errores
        }
        try {
            order.setId(id);
            customerOrderService.updateOrder(order);
            redirectAttributes.addFlashAttribute("successMessage", "Pedido actualizado exitosamente!");
            return "redirect:/web/orders";
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("order", order);
            return "redirect:/web/orders/edit/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al actualizar pedido: " + e.getMessage());
            redirectAttributes.addFlashAttribute("order", order);
            return "redirect:/web/orders/edit/" + id;
        }
    }

    // 6. Eliminar una orden
    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerOrderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Pedido eliminado exitosamente!");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al eliminar pedido: " + e.getMessage());
        }
        return "redirect:/web/orders";
    }

    // 7. Mostrar detalles de una orden
    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CustomerOrder> orderOptional = customerOrderService.getOrderById(id);
        if (orderOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Pedido no encontrado.");
            return "redirect:/web/orders";
        }
        model.addAttribute("order", orderOptional.get());
        model.addAttribute("pageTitle", "Detalles del Pedido");
        model.addAttribute("contentFragment", "orders/orderDetailContent");
        return "layouts/main";
    }
}