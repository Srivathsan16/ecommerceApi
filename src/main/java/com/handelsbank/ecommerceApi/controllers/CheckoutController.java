package com.handelsbank.ecommerceApi.controllers;

import com.handelsbank.ecommerceApi.exceptions.CustomMessage;
import com.handelsbank.ecommerceApi.exceptions.NoItemsToCheckoutException;
import com.handelsbank.ecommerceApi.services.CheckoutService;
import com.handelsbank.ecommerceApi.utilities.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class CheckoutController {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);
    private final CheckoutService checkoutService;
    private final Validator<Long> productValidator;

    public CheckoutController(CheckoutService checkoutService, Validator<Long> productValidator) {
        this.checkoutService = checkoutService;
        this.productValidator = productValidator;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody List<Long> productIds) {
        logger.info("Checking out with product IDs: {}", productIds);
        // Validate product IDs over engineering ? check this part
        productValidator.validateList(productIds,
                Objects::nonNull,
                new NoItemsToCheckoutException(CustomMessage.NO_ITEMS_TO_CHECKOUT.getMessage()));

        BigDecimal total = checkoutService.calculateTotalPrice(productIds);
        return ResponseEntity.ok(Map.of("price", total));
    }
}
