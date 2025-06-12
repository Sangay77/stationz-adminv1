package com.station.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;

    @Transient
    public float getSubTotal() {
        float originalPrice = product.getPrice();
        float discountPercent = product.getDiscountPercent();
        float finalPrice = originalPrice;

        if (discountPercent > 0) {
            finalPrice = originalPrice - (discountPercent / 100.0f * originalPrice);
        }

        return finalPrice * quantity;
    }

}
