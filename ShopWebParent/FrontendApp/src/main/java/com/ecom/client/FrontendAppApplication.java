package com.ecom.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.ecom.common.entity", "com.ecom.client.repository"})
public class FrontendAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendAppApplication.class, args);
    }

}
