package com.bintang.auditrail_entity.controller;

import com.bintang.auditrail_entity.model.Produk;
import com.bintang.auditrail_entity.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produk")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public Produk buatProduk(@RequestBody Produk produk) {
        return productRepository.save(produk);
    }

    @GetMapping
    public List<Produk> semuaProduk() {
        return productRepository.findAll();
    }
}