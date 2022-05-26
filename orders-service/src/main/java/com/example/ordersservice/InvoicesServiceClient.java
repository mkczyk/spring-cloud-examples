package com.example.ordersservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("invoices-service")
public interface InvoicesServiceClient {

    @GetMapping("invoice/generate")
    void generateInvoice();

}
