package com.station.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "product")  // Add this to break circular reference
@EqualsAndHashCode(exclude = "product")  // Also exclude from equals/hashCode
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public ProductImage() {
    }

    @Transient
    public String getImagePath() {
        if (name == null || product == null) return "/images/default-user.png";
        return "/product-images/" + product.getId() + "/extras/" + this.name;
    }
}