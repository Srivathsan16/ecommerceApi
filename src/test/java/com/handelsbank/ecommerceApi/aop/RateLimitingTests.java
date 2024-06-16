package com.handelsbank.ecommerceApi.aop;

import com.handelsbank.ecommerceApi.controllers.CheckoutController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RateLimitingTests {

    @Autowired
    private CheckoutController checkoutController;

    @Test
    public void testRateLimiting() throws Exception {
        assertThrows(Exception.class, () -> {
            for (int i = 0; i <= 100; i++) {
                checkoutController.checkout(Arrays.asList(1L, 2L, 3L));
            }
        }, "Rate limit exceeded");
    }
}

