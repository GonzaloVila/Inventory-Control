package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryControlApplication {

	public static void main(String[] args) {
		System.out.println("CORRIENDO");
		SpringApplication.run(InventoryControlApplication.class, args);
	}

}
