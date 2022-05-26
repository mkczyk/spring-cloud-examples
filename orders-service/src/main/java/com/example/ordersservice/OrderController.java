package com.example.ordersservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final EmailServiceClient emailServiceClient;
    private final InvoicesServiceClient invoicesServiceClient;

    @GetMapping("order/add")
    public void addOrder() {
        log.info("Adding order");
        emailServiceClient.sendEmail();
        invoicesServiceClient.generateInvoice();
    }

}
