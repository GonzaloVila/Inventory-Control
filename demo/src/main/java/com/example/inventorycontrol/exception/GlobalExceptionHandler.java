package com.example.inventorycontrol.exception;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicate(DuplicateResourceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<String> handleInvalidDataAccessApiUsage(InvalidDataAccessApiUsageException ex) {
        System.err.println("--- InvalidDataAccessApiUsageException Capturada ---");
        System.err.println("Mensaje: " + ex.getMessage());
        if (ex.getCause() != null) {
            System.err.println("Causa Raíz: " + ex.getCause().getMessage());
            ex.getCause().printStackTrace(); // Imprime la pila de la causa raíz
        }
        ex.printStackTrace(); // excepción original
        System.err.println("----------------------------------------------");

        // Puedes devolver un mensaje más detallado para el cliente o uno genérico
        return new ResponseEntity<>("Error en la operación de datos. " + ex.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        // Asegúrate de imprimir la pila para cualquier otra excepción no controlada
        System.err.println("--- Excepción Genérica Capturada ---");
        System.err.println("Mensaje: " + ex.getMessage());
        ex.printStackTrace();
        System.err.println("------------------------------------");
        return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}