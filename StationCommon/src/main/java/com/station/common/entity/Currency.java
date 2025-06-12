package com.station.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "currencies")
@Data
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String symbol;
    private String code;

    public Currency(String name, String symbol, String code) {
        this.name = name;
        this.symbol = symbol;
        this.code = code;
    }

    public Currency() {
    }

    @Override
    public String toString() {
        return name + "-" + code + "-" + symbol;
    }
}
