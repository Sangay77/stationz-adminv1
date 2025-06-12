package com.station;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class StationFrontEndApplication {

    public static void main(String[] args) {

        SpringApplication.run(StationFrontEndApplication.class, args);



    }

    @PostConstruct
    public void printEnvVars() {
        System.out.println("GOOGLE_CLIENT_ID = " + System.getenv("GOOGLE_CLIENT_ID"));
        System.out.println("GOOGLE_CLIENT_SECRET = " + System.getenv("GOOGLE_CLIENT_SECRET"));
    }

}