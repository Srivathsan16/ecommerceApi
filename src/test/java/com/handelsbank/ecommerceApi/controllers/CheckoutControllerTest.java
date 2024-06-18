package com.handelsbank.ecommerceApi.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.handelsbank.ecommerceApi.controllers.CheckoutController;
import com.handelsbank.ecommerceApi.exceptions.NoItemsToCheckoutException;
import com.handelsbank.ecommerceApi.model.CheckoutResponse;
import com.handelsbank.ecommerceApi.services.ICheckoutService;
import com.handelsbank.ecommerceApi.utilities.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CheckoutControllerTest {

    @Mock
    private ICheckoutService checkoutService;

    @Mock
    private Validator<Long> productValidator;

    private CheckoutController checkoutController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        checkoutController = new CheckoutController(checkoutService, productValidator);
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

    @Test
    public void whenEmptyProductIds_thenExpectNoItemsToCheckoutException() {
        List<Long> productIds = Arrays.asList();
        doThrow(new NoItemsToCheckoutException("No items to checkout"))
                .when(productValidator).validateList(eq(productIds), any(), any());

        NoItemsToCheckoutException exception = assertThrows(NoItemsToCheckoutException.class, () -> {
            checkoutController.checkout(productIds);
        });

        assertEquals("No items to checkout", exception.getMessage());
    }

    @Test
    public void whenValidProductIds_thenCalculateTotalPrice() {
        List<Long> productIds = Arrays.asList(1L, 2L);
        BigDecimal expectedTotal = new BigDecimal("300.00");
        when(checkoutService.calculateTotalPrice(productIds)).thenReturn(expectedTotal);

        ResponseEntity<?> response = checkoutController.checkout(productIds);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof CheckoutResponse);
        assertEquals(expectedTotal, ((CheckoutResponse) response.getBody()).price());
    }

    @Test
    public void whenEmptyProductIds_thenReturnZero() {
        List<Long> productIds = Arrays.asList();
        when(checkoutService.calculateTotalPrice(productIds)).thenReturn(BigDecimal.ZERO);

        ResponseEntity<?> response = checkoutController.checkout(productIds);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.ZERO, ((CheckoutResponse) response.getBody()).price());
    }
}
