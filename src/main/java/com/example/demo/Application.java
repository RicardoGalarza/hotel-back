package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.out.println("Valor de GOOGLE_CREDENTIALS_JSON: " + System.getenv("GOOGLE_CREDENTIALS_JSON"));
        SpringApplication.run(Application.class, args);
    }
}

