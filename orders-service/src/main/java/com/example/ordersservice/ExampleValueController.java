package com.example.ordersservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExampleValueController {

    @Value("${example.value}")
    private String value;

    @GetMapping("value")
    @PostConstruct
    public String addOrder() {
        log.info("Example value from configuration: {}", value);
        return value;
    }

}
