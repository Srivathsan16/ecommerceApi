package com.handelsbank.ecommerceApi.controllers;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.handelsbank.ecommerceApi.exceptions.CustomMessage;
import com.handelsbank.ecommerceApi.exceptions.NoItemsToCheckoutException;
import com.handelsbank.ecommerceApi.services.CheckoutService;
import com.handelsbank.ecommerceApi.utilities.Validator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CheckoutControllerTest {

    @Mock
    private CheckoutService checkoutService;

    @Mock
    private Validator<Long> productValidator;

    @InjectMocks
    private CheckoutController checkoutController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenInvalidProductIds_thenExpectIllegalArgumentException() {
        List<Long> productIds = Arrays.asList(1L, null, -1L);
        doThrow(new IllegalArgumentException("Invalid product ID"))
                .when(productValidator).validateList(eq(productIds), any(), any());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            checkoutController.checkout(productIds);
        });

        assertEquals("Invalid product ID", exception.getMessage());
    }

    @Test
    public void whenCheckoutServiceThrowsException_thenPropagateIt() {
        List<Long> productIds = Arrays.asList(1L, 2L);
        when(checkoutService.calculateTotalPrice(productIds))
                .thenThrow(new RuntimeException("Service failure"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            checkoutController.checkout(productIds);
        });

        assertEquals("Service failure", exception.getMessage());
    }

}

