package com.handelsbank.ecommerceApi.repository;

import com.handelsbank.ecommerceApi.entities.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {
    List<DiscountEntity> findByProductId(Long productId);
}