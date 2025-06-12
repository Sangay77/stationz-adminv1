package com.station.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;
    @Column(length = 128, nullable = false, unique = true)
    private String alias;
    @Column(length = 128, nullable = false)
    private String image;
    private boolean enabled;

    public Category(String name) {
        this.name = name;
        this.alias = name.toLowerCase().replace(" ", "-");
        this.image = "default.jpeg";
    }

    public Category() {
    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Transient
    public String getPhotosImagePath() {
        if (image == null || image.isEmpty()) return "/images/default-user.png";
        return "/category-image/" + this.id + "/" + this.image;
    }
}
