package com.example.ordersservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("email-service")
public interface EmailServiceClient {

    @GetMapping("email/send")
    void sendEmail();

}
