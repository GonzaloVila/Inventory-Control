package com.example.inventorycontrol.controller.web;

import com.example.inventorycontrol.exception.ResourceNotFoundException;
import com.example.inventorycontrol.model.Employee;
import com.example.inventorycontrol.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return "employees/employeeListContent";
    }

    // Mostrar el formulario para crear un nuevo empleado
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @GetMapping("/new")
    public String showNewEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/employeeFormContent";
    }

    // Procesar el formulario de creación de un nuevo empleado
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @PostMapping("/new")
    public String createEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        employeeService.createEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Empleado creado exitosamente.");
        return "redirect:/web/employees";
    }

    // Mostrar los detalles de un empleado
    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_MANAGER')")
    @GetMapping("/{id}")
    public String showEmployeeDetails(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado con ID " + id + " no encontrado."));
        model.addAttribute("employee", employee);
        return "employees/employeeDetailContent";
    }

    // Mostrar el formulario para editar un empleado existente
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado con ID " + id + " no encontrado."));
        model.addAttribute("employee", employee);
        return "employees/employeeFormContent";
    }

    // Procesar el formulario de edición de un empleado
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        employee.setId(id);
        employeeService.updateEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Empleado actualizado exitosamente.");
        return "redirect:/web/employees";
    }

    // Eliminar un empleado
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("successMessage", "Empleado eliminado exitosamente.");
        return "redirect:/web/employees";
    }
}