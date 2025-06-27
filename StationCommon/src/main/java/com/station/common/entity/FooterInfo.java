package com.station.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FooterInfo {

    @Id
    private Long id;

    private String about;
    private String location;
    private String email;
    private String phone;

    private String facebookUrl;
    private String instagramUrl;
    private String twitterUrl;
}

