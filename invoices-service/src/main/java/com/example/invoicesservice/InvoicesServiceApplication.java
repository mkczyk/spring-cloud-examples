package com.example.invoicesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InvoicesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoicesServiceApplication.class, args);
    }

}
