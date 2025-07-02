package com.bintang.controller;

import com.bintang.entity.Product;
import com.bintang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String listProducts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "6") int size) {
        Page<Product> productPage = productService.getPaginatedProducts(page, size);
        model.addAttribute("productPage", productPage);
        return "product_list";
    }
}

