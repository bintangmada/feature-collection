package com.bintang.auditrail_entity.repository;

import com.bintang.auditrail_entity.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}

