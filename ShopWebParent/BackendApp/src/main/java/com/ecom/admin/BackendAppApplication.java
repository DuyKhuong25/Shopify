package com.ecom.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.ecom.common.entity", "com.ecom.admin.repository"})
public class BackendAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendAppApplication.class, args);
	}

}
