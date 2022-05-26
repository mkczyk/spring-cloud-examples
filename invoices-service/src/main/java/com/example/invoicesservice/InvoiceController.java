package com.example.invoicesservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InvoiceController {

    private final EmailServiceClient emailServiceClient;

    @GetMapping("invoice/generate")
    public void generateInvoice() {
        log.info("Generating invoice");
        emailServiceClient.sendEmail();
    }

}
