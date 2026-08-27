package com.devsecops.devsecopsapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "DevSecOps CI/CD Pipeline Application is running successfully!";
    }
}
