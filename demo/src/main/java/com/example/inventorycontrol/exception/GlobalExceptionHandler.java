package com.example.inventorycontrol.exception;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidDataAccessApiUsage(InvalidDataAccessApiUsageException ex) {
        System.err.println("--- InvalidDataAccessApiUsageException Capturada ---");
        System.err.println("Mensaje: " + ex.getMessage());
        if (ex.getCause() != null) {
            System.err.println("Causa Raíz: " + ex.getCause().getMessage());
            ex.getCause().printStackTrace();
        }
        ex.printStackTrace();
        System.err.println("----------------------------------------------");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("message", "Error en la operación de datos: " + ex.getMessage());
        errorDetails.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        System.err.println("--- DataIntegrityViolationException Capturada ---");
        System.err.println("Mensaje: " + ex.getMessage());
        ex.printStackTrace();
        System.err.println("------------------------------------");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        String userMessage = "No se puede eliminar el registro porque tiene elementos relacionados (ej. productos de este proveedor están en pedidos). " +
                "Por favor, elimine o desasocie los elementos relacionados primero.";
        if (ex.getCause() != null && ex.getCause().getMessage() != null && ex.getCause().getMessage().contains("Cannot delete or update a parent row")) {

        }

        errorDetails.put("message", userMessage);
        errorDetails.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT); // HTTP 409
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        System.err.println("--- Excepción Genérica Capturada ---");
        System.err.println("Mensaje: " + ex.getMessage());
        ex.printStackTrace();
        System.err.println("------------------------------------");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetails.put("message", "Error interno del servidor. Por favor, intente de nuevo más tarde.");
        errorDetails.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}