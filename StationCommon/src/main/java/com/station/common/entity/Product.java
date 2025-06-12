package com.station.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.*;

@Entity
@Data
@ToString(exclude = {"images", "category"})  // Add this line to exclude circular references
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true)
    private String alias;

    @Column(name = "short_description", length = 512)
    private String shortDescription;

    @Column(name = "full_description", length = 4096)
    private String fullDescription;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    private boolean enabled;

    @Column(name = "in_stock")
    private boolean inStock;

    private float cost;
    private float price;

    @Column(name = "discount_percent")
    private float discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDetail> details = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Transient
    public String getMainImagePath() {
        if (mainImage == null || mainImage.isEmpty()) return "/images/default-user.png";
        return "/product-images/" + this.id + "/" + this.mainImage;
    }

    @Transient
    public String getShortName() {
        if (name == null) return "";
        return name.length() > 70 ? name.substring(0, 70) + "..." : name;
    }


    public Product(Integer id) {
        this.id = id;
    }

    public Product() {
    }

    public void addExtraImage(String imageName) {
        this.images.add(new ProductImage(imageName, this));
    }

    public void addDetail(String name, String value) {
        this.details.add(new ProductDetail(name, value, this));
    }
}