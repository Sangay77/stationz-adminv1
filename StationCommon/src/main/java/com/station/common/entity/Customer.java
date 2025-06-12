package com.station.common.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "customer")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = false, length = 45)
    private String email;
    @Column(nullable = false, length = 64)
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    @Column(name = "postal_code")
    private String postalCode;
    private boolean enabled;
    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "verification_code")

    private String verificationCode;
    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type")
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token",length = 30, nullable = true)
    private String resetPasswordToken;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
