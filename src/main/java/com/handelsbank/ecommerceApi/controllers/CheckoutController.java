package com.handelsbank.ecommerceApi.controller;


import com.handelsbank.ecommerceApi.services.CheckoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class CheckoutController {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody List<Long> productIds) {
        logger.info("Product IDs from the request body are: {} ",  productIds);
        BigDecimal total = checkoutService.calculateTotalPrice(productIds);
        return ResponseEntity.ok(Map.of("price", total));
    }
}
