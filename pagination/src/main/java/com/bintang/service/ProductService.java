package com.bintang.service;

import com.bintang.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://fakestoreapi.com/products";

    public List<Product> getAllProducts() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity(API_URL, Product[].class);
        return Arrays.asList(response.getBody());
    }

    public Page<Product> getPaginatedProducts(int page, int size) {
        List<Product> allProducts = getAllProducts();
        int start = Math.min(page * size, allProducts.size());
        int end = Math.min(start + size, allProducts.size());
        List<Product> paginatedList = allProducts.subList(start, end);
        return new PageImpl<>(paginatedList, PageRequest.of(page, size), allProducts.size());
    }
}

