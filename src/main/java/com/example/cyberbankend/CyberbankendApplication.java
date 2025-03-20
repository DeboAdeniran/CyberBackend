package com.example.cyberbankend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.cyberbankend")
public class CyberbankendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CyberbankendApplication.class, args);
    }

}
