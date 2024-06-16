/*
package com.handelsbank.ecommerceApi.integration;



import com.handelsbank.ecommerceApi.entities.DiscountEntity;
import com.handelsbank.ecommerceApi.entities.ProductEntity;
import com.handelsbank.ecommerceApi.repository.DiscountRepository;
import com.handelsbank.ecommerceApi.repository.ProductRepository;
import com.handelsbank.ecommerceApi.services.CheckoutService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MySQLContainer;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@Transactional
public class IntegrationTests {

    @Container
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @BeforeEach
    void setup() {
        ProductEntity product1 = new ProductEntity();
        product1.setName("Product 1");
        product1.setPrice(new BigDecimal("100.00"));
        productRepository.save(product1);

        ProductEntity product2 = new ProductEntity();
        product2.setName("Product 2");
        product2.setPrice(new BigDecimal("200.00"));
        productRepository.save(product2);

        DiscountEntity discount1 = new DiscountEntity();
        discount1.setProduct(product1);
        discount1.setDiscountedPrice(new BigDecimal("90.00"));
        discount1.setQuantityRequired(2);
        discountRepository.save(discount1);
    }

    @Test
    public void testCheckoutProcessSuccess() {
        List<Long> productIds = Arrays.asList(1L, 1L, 2L);
        BigDecimal total = checkoutService.calculateTotalPrice(productIds);
        assertNotNull(total);
        assertEquals(0, total.compareTo(new BigDecimal("390.00")), "Total price calculated is incorrect.");
    }

    @Test
    public void testProductNotFound() {
        List<Long> productIds = Arrays.asList(999L); // Non-existing product
        Exception exception = assertThrows(RuntimeException.class, () -> {
            checkoutService.calculateTotalPrice(productIds);
        });
        assertTrue(exception.getMessage().contains("not found"));
    }
}
*/

