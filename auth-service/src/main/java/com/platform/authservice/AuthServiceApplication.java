package com.platform.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.platform")
public class AuthServiceApplication {

    public static void main(String[] args) {
        System.out.println("YAYYY");
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
