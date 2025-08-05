package com.example.inventorycontrol.controller.web;

import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Employee;
import com.example.inventorycontrol.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/employees")
public class EmployeeWebController {

    @Autowired
    private EmployeeService employeeService;

    // Mostrar la lista de empleados
    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_MANAGER')")
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("contentFragment", "employees/employeeListContent");
        return "layouts/main";
    }

    // Mostrar el formulario para crear un nuevo empleado
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @GetMapping("/new")
    public String showNewEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("contentFragment", "employees/employeeFormContent");
        return "layouts/main";
    }

    // Mostrar el formulario para editar un empleado existente
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado con ID " + id + " no encontrado."));
        model.addAttribute("employee", employee);
        model.addAttribute("contentFragment", "employees/employeeFormContent");
        return "layouts/main";
    }

    // Unico metodo para procesar el formulario de creacion o edicion de un empleado
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        if (employee.getId() == null) {
            // Lógica para crear un nuevo empleado
            employeeService.createEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Empleado creado exitosamente.");
        } else {
            // Lógica para actualizar un empleado existente
            employeeService.updateEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Empleado actualizado exitosamente.");
        }
        return "redirect:/web/employees";
    }

    // Mostrar los detalles de un empleado
    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_MANAGER')")
    @GetMapping("/{id}")
    public String showEmployeeDetails(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado con ID " + id + " no encontrado."));
        model.addAttribute("employee", employee);
        model.addAttribute("contentFragment", "employees/employeeDetailContent");
        return "layouts/main";
    }

    // Eliminar un empleado
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Empleado eliminado exitosamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se puede eliminar el empleado porque está asociado a otros registros (ej. productos, pedidos, etc.).");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al intentar eliminar el empleado.");
        }
        return "redirect:/web/employees";
    }
}