package com.handelsbank.ecommerceApi.services;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.handelsbank.ecommerceApi.entities.DiscountEntity;
import com.handelsbank.ecommerceApi.entities.ProductEntity;
import com.handelsbank.ecommerceApi.repository.DiscountRepository;
import com.handelsbank.ecommerceApi.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CheckoutServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private ProductEntity createProduct(Long productId, BigDecimal price) {
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        product.setPrice(price);
        return product;
    }

    private DiscountEntity createDiscount(int quantityRequired, BigDecimal discountedPrice) {
        DiscountEntity discount = new DiscountEntity();
        discount.setQuantityRequired(quantityRequired);
        discount.setDiscountedPrice(discountedPrice);
        return discount;
    }

    @Test
    public void whenValidProductIds_thenCalculateTotalPrice() {
        Long productId = 1L;
        ProductEntity product = createProduct(productId, new BigDecimal("100.00"));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(discountRepository.findByProductId(productId)).thenReturn(List.of());

        BigDecimal totalPrice = checkoutService.calculateTotalPrice(Arrays.asList(productId));

        assertEquals(new BigDecimal("100.00"), totalPrice);
        verify(productRepository).findById(productId);
        verify(discountRepository).findByProductId(productId);
    }

    @Test
    public void whenProductNotFound_thenThrowEntityNotFoundException() {
        Long productId = 99L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> checkoutService.calculateTotalPrice(Arrays.asList(productId)));

        assertTrue(thrown.getMessage().contains("Product with ID " + productId + " not found."));
    }

    @Test
    public void whenDiscountsApply_thenCalculateDiscountedPrice() {
        Long productId = 1L;
        ProductEntity product = createProduct(productId, new BigDecimal("100.00"));
        DiscountEntity discount = createDiscount(2, new BigDecimal("150.00"));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(discountRepository.findByProductId(productId)).thenReturn(List.of(discount));

        BigDecimal totalPrice = checkoutService.calculateTotalPrice(Arrays.asList(productId, productId));

        assertEquals(new BigDecimal("150.00"), totalPrice);  // 2 for 150 instead of 200
        verify(discountRepository).findByProductId(productId);
    }

    @Test
    public void whenNoDiscountsApply_thenCalculateNormalPrice() {
        Long productId = 1L;
        ProductEntity product = createProduct(productId, new BigDecimal("100.00"));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(discountRepository.findByProductId(productId)).thenReturn(List.of());

        BigDecimal totalPrice = checkoutService.calculateTotalPrice(Arrays.asList(productId, productId));

        assertEquals(new BigDecimal("200.00"), totalPrice);
    }
}

