package com.example.demo.controller;


import com.example.demo.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/infoProducto")
    public String MostrarInfo(){
        return "Info_Del_Producto";
    }
}
