package com.example.inventorycontrol.controller.web;

import com.example.inventorycontrol.payload.request.LoginRequest;
import com.example.inventorycontrol.payload.request.SignupRequest;
import com.example.inventorycontrol.security.services.RegistrationService;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web")
public class AuthWebController {

    @Autowired
    private RegistrationService registrationService;

    // --- LOGIN ---
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("loginRequest")) {
            model.addAttribute("loginRequest", new LoginRequest());
        }
        return "auth/login";
    }

    // Opcional: Redirigir la raíz a tu dashboard si el usuario ya está autenticado

    @GetMapping("/")
    public String redirectToDashboardIfAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            return "redirect:/web/dashboard";
        }
        return "redirect:/web/login";
    }


    // --- REGISTER ---
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("signupRequest")) {
            model.addAttribute("signupRequest", new SignupRequest());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("signupRequest") SignupRequest signupRequest,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.signupRequest", bindingResult);
            redirectAttributes.addFlashAttribute("signupRequest", signupRequest);
            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            return "redirect:/web/register";
        }

        try {
            registrationService.registerUser(signupRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Registro exitoso. ¡Ahora puedes iniciar sesión!");
            return "redirect:/web/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("signupRequest", signupRequest);
            return "redirect:/web/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al registrar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("signupRequest", signupRequest);
            return "redirect:/web/register";
        }
    }

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                model.addAttribute("username", username);
            } else {
                model.addAttribute("username", "Usuario Desconocido");
            }
        }
        model.addAttribute("pageTitle", "Dashboard de Inventario");
        model.addAttribute("contentFragment", "dashboardContent");
        return "layouts/main";
    }
}