package com.bintang.auditrail_entity.repository;

import com.bintang.auditrail_entity.model.Produk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Produk, Long> {
}
