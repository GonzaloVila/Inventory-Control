package com.example.inventorycontrol.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class SignupRequest {
    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres.")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Size(min = 6, max = 40, message = "La contraseña debe tener entre 6 y 40 caracteres.")
    private String password;

    @NotBlank(message = "El email no puede estar vacío.")
    @Size(max = 50, message = "El email no puede exceder los 50 caracteres.")
    @Email(message = "Debe ser un formato de email válido.")
    private String email;

    private Set<String> rol; // Opcional: para registrar con roles específicos


    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; } // Getter para email
    public void setEmail(String email) { this.email = email; } // Setter para email
    public Set<String> getRol() { return rol; }
    public void setRol(Set<String> rol) { this.rol = rol; }
}