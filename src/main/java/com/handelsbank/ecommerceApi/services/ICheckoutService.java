package com.handelsbank.ecommerceApi.services;

import com.handelsbank.ecommerceApi.entities.DiscountEntity;
import com.handelsbank.ecommerceApi.entities.ProductEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ICheckoutService {
    BigDecimal calculateTotalPrice(List<Long> productIds);
    BigDecimal calculatePriceForProduct(Map.Entry<Long, Integer> entry, ProductEntity product);
    BigDecimal calculateDiscountPrice(long count, DiscountEntity discount, ProductEntity product);
}

