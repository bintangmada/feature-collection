package com.bintang.auditrail_entity.controller;
import com.bintang.auditrail_entity.model.Product;
import com.bintang.auditrail_entity.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired private ProductRepository productRepository;

    private String currentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product) {
        product.setCreatedBy(currentUser());
        product.setCreatedDate(LocalDateTime.now());
        product.setDeleted(false);
        product.setStatus("AKTIF");
        return ResponseEntity.ok(productRepository.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product p) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(p.getName());
        product.setPrice(p.getPrice());
        product.setUpdatedBy(currentUser());
        product.setUpdatedDate(LocalDateTime.now());
        return ResponseEntity.ok(productRepository.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setDeleted(true);
        product.setDeletedBy(currentUser());
        product.setDeletedDate(LocalDateTime.now());
        product.setStatus("NONAKTIF");
        return ResponseEntity.ok(productRepository.save(product));
    }
}