package com.handelsbank.ecommerceApi.services;
import com.handelsbank.ecommerceApi.entities.DiscountEntity;
import com.handelsbank.ecommerceApi.entities.ProductEntity;
import com.handelsbank.ecommerceApi.repository.DiscountRepository;
import com.handelsbank.ecommerceApi.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckoutService implements ICheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    public CheckoutService(ProductRepository productRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    @Override
    public BigDecimal calculateTotalPrice(List<Long> productIds) {
        logger.info("Calculating total price for products with IDs: {}", productIds);
        if (productIds.isEmpty()) {
            return BigDecimal.ZERO;
        }

        Map<Long, Integer> countMap = productIds.stream()
                .collect(Collectors.toMap(id -> id, id -> 1, Integer::sum));

        List<ProductEntity> products = productRepository.findAllById(countMap.keySet());
        Map<Long, ProductEntity> productMap = products.stream()
                .collect(Collectors.toMap(ProductEntity::getId, product -> product));

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : countMap.entrySet()) {
            ProductEntity product = productMap.get(entry.getKey());
            if (product == null) {
                throw new EntityNotFoundException("Product with ID " + entry.getKey() + " not found.");
            }
            totalPrice = totalPrice.add(calculatePriceForProduct(entry, product));
        }
        return totalPrice;
    }

    @Override
    public BigDecimal calculatePriceForProduct(Map.Entry<Long, Integer> entry, ProductEntity product) {
        List<DiscountEntity> discounts = discountRepository.findByProductId(product.getId());
        long count = entry.getValue();
        BigDecimal totalNormalPrice = product.getPrice().multiply(BigDecimal.valueOf(count));

        return discounts.stream()
                .filter(d -> count >= d.getQuantityRequired())
                .findFirst()
                .map(discount -> calculateDiscountPrice(count, discount, product))
                .orElse(totalNormalPrice);
    }

    @Override
    public BigDecimal calculateDiscountPrice(long count, DiscountEntity discount, ProductEntity product) {
        long discountBundles = count / discount.getQuantityRequired();
        long normalPricedProducts = count % discount.getQuantityRequired();

        BigDecimal discountPrice = discount.getDiscountedPrice()
                .multiply(BigDecimal.valueOf(discountBundles));
        BigDecimal normalPrice = product.getPrice()
                .multiply(BigDecimal.valueOf(normalPricedProducts));

        return discountPrice.add(normalPrice);
    }
}
