package com.fitness.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SimpleApp {

    public static void main(String[] args) {
        System.out.println("=== STARTING SIMPLE APP ===");
        SpringApplication.run(SimpleApp.class, args);
        System.out.println("=== SIMPLE APP STARTED ===");
    }

    @GetMapping("/test")
    public String test() {
        return "Simple test works!";
    }
}