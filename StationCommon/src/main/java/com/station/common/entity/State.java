package com.station.common.entity;


import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "states")
@Data
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Version
    private Integer version;

    public State(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public State() {
    }
}
