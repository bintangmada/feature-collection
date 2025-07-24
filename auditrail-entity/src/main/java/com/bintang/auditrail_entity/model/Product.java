package com.bintang.auditrail_entity.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;

    public Product(String createdBy, LocalDateTime createdDate, String updatedBy, LocalDateTime updatedDate, String deletedBy, LocalDateTime deletedDate, Boolean deleted, String status, Long id, String name, Double price) {
        super(createdBy, createdDate, updatedBy, updatedDate, deletedBy, deletedDate, deleted, status);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
