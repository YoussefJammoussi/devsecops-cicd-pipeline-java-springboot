package com.devsecops.devsecopsapp.controller;

import com.devsecops.devsecopsapp.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @GetMapping("/api/products")
    public List<Product> getProducts() {

        return List.of(
                new Product(1L, "Laptop", 2500.0),
                new Product(2L, "Smartphone", 1200.0),
                new Product(3L, "Keyboard", 150.0)
        );
    }
}
