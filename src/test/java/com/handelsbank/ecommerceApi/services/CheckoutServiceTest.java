package com.handelsbank.ecommerceApi.services;

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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckoutServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountRepository discountRepository;
    @InjectMocks
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    private ProductEntity createProduct(Long id, String price) {
        ProductEntity product = new ProductEntity();
        product.setId(id);
        product.setPrice(new BigDecimal(price));
        return product;
    }


    private DiscountEntity createDiscount(int requiredQuantity, String discountPrice) {
        DiscountEntity discount = new DiscountEntity();
        discount.setQuantityRequired(requiredQuantity);
        discount.setDiscountedPrice(new BigDecimal(discountPrice));
        return discount;
    }


    @Test
    void whenCalculateTotalPrice_givenValidProducts_thenCalculateCorrectTotal() {
        List<ProductEntity> products = Arrays.asList(createProduct(1L, "100.00"), createProduct(2L, "200.00"));
        when(productRepository.findAllById(anySet())).thenReturn(products);
        when(discountRepository.findByProductId(anyLong())).thenReturn(Collections.emptyList());

        BigDecimal totalPrice = checkoutService.calculateTotalPrice(Arrays.asList(1L, 2L, 2L));
        assertEquals(new BigDecimal("500.00"), totalPrice);
    }


    @Test
    void whenCalculatePriceForProduct_givenNoDiscount_thenCalculateNormalPrice() {
        ProductEntity product = createProduct(1L, "100.00");
        Map.Entry<Long, Integer> entry = new AbstractMap.SimpleEntry<>(1L, 1);
        when(discountRepository.findByProductId(1L)).thenReturn(Collections.emptyList());

        BigDecimal price = checkoutService.calculatePriceForProduct(entry, product);
        assertEquals(new BigDecimal("100.00"), price);
    }


    @Test
    void whenCalculateDiscountPrice_givenValidDiscount_thenApplyDiscount() {
        ProductEntity product = createProduct(1L, "100.00");
        DiscountEntity discount = createDiscount(1, "90.00");

        BigDecimal discountPrice = checkoutService.calculateDiscountPrice(1, discount, product);
        assertEquals(new BigDecimal("90.00"), discountPrice);
    }


    @Test
    void whenCalculateTotalPrice_givenMissingProduct_thenThrowException() {
        when(productRepository.findAllById(anySet())).thenReturn(Collections.emptyList());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> checkoutService.calculateTotalPrice(Arrays.asList(1L)));
        assertTrue(exception.getMessage().contains("Product with ID 1 not found"));
    }


    @Test
    void whenCalculateTotalPrice_givenEmptyProductList_thenReturnZero() {
        BigDecimal totalPrice = checkoutService.calculateTotalPrice(Collections.emptyList());
        assertEquals(BigDecimal.ZERO, totalPrice, "Total price should be zero when no products are provided.");
    }


    @Test
    void whenCalculatePriceForProduct_givenProductWithNoQualifyingDiscounts_thenReturnNormalPriceOnly() {
        ProductEntity product = createProduct(1L, "100.00");
        Map.Entry<Long, Integer> entry = new AbstractMap.SimpleEntry<>(1L, 1);
        when(discountRepository.findByProductId(1L)).thenReturn(Collections.emptyList());

        BigDecimal price = checkoutService.calculatePriceForProduct(entry, product);
        assertEquals(new BigDecimal("100.00"), price, "Price should be normal when no discounts apply.");
    }


    @Test
    void whenCalculatePriceForProduct_givenInsufficientQuantityForDiscount_thenReturnNormalPrice() {
        ProductEntity product = createProduct(1L, "100.00");
        DiscountEntity discount = createDiscount(3, "80.00");  // Discount requires buying 3
        Map.Entry<Long, Integer> entry = new AbstractMap.SimpleEntry<>(1L, 2);
        when(discountRepository.findByProductId(1L)).thenReturn(List.of(discount));

        BigDecimal price = checkoutService.calculatePriceForProduct(entry, product);
        assertEquals(new BigDecimal("200.00"), price, "Price should be normal when quantity is not enough for discounts.");
    }


    @Test
    void whenDiscountRepositoryThrows_thenHandleExceptionGracefully() {
        ProductEntity product = createProduct(1L, "100.00");
        Map.Entry<Long, Integer> entry = new AbstractMap.SimpleEntry<>(1L, 1);
        when(discountRepository.findByProductId(1L)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> checkoutService.calculatePriceForProduct(entry, product));
        assertEquals("Database error", exception.getMessage(), "Should handle and relay repository errors.");
    }
}
