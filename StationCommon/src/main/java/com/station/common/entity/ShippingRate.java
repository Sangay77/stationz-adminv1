package com.station.common.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Table(name = "shipping_rates")
@Data
public class ShippingRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private float rate;
    private int days;

    @Column(name = "code_supported")
    private boolean codeSupported;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false,length = 45)
    private String state;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ShippingRate that)) return false;
        return Float.compare(rate, that.rate) == 0 && days == that.days && codeSupported == that.codeSupported && Objects.equals(id, that.id) && Objects.equals(country, that.country) && Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rate, days, codeSupported, country, state);
    }
}
