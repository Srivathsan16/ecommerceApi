package com.handelsbank.ecommerceApi.services;

import com.handelsbank.ecommerceApi.entities.DiscountEntity;
import com.handelsbank.ecommerceApi.entities.ProductEntity;
import com.handelsbank.ecommerceApi.repository.DiscountRepository;
import com.handelsbank.ecommerceApi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    public CheckoutService(ProductRepository productRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    public BigDecimal calculateTotalPrice(List<Long> productIds) {
        Map<Long, Integer> countMap = productIds.stream()
                .collect(Collectors.toMap(
                        e -> e,
                        e -> 1,
                        Integer::sum
                ));

        BigDecimal totalPrice = BigDecimal.valueOf(0);
        for (Map.Entry<Long, Integer> entry : countMap.entrySet()) {
            ProductEntity product = productRepository.findById(entry.getKey()).orElse(null);
            if (product == null) continue;
            List<DiscountEntity> discounts = discountRepository.findByProductId(product.getId());
            long count = entry.getValue();

            DiscountEntity applicableDiscount = discounts.stream()
                    .filter(d -> count >= d.getQuantityRequired())
                    .findFirst().orElse(null);

            if (applicableDiscount != null) {
                long discountBundles = count / applicableDiscount.getQuantityRequired();
                long normalPricedProducts = count % applicableDiscount.getQuantityRequired();

                // Calculate the total price for products with discount
                BigDecimal totalDiscountPrice = applicableDiscount.getDiscountedPrice()
                        .multiply(BigDecimal.valueOf(discountBundles));

                // Calculate the total price for products without the discount
                BigDecimal totalNormalPrice = product.getPrice()
                        .multiply(BigDecimal.valueOf(normalPricedProducts));


                totalPrice = totalPrice.add(totalDiscountPrice).add(totalNormalPrice);
            } else {
                // Calculate the total price without any discounts
                BigDecimal totalNormalPrice = product.getPrice()
                        .multiply(BigDecimal.valueOf(count));
                totalPrice = totalPrice.add(totalNormalPrice);
            }
        }

        return totalPrice;
    }
}
