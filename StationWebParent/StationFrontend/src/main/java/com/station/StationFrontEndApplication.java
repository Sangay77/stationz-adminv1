package com.station;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class StationFrontEndApplication {

    public static void main(String[] args) {

        SpringApplication.run(StationFrontEndApplication.class, args);

    }

}