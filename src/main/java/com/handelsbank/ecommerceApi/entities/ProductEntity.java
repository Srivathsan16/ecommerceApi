package com.handelsbank.ecommerceApi.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Format ID as needed for display - check whethet this logic is needded little confusing maybe overthinking
    @Transient
    private String formattedId;

    private String name;
    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscountEntity> discounts;

    // Getter and setter for ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // TODO: Format ID as needed for display - check whethet this logic is needded little confusing maybe overthinking
    public String getFormattedId() {
        return String.format("%03d", id);
    }

    public void setFormattedId(String formattedId) {
        this.formattedId = formattedId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<DiscountEntity> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<DiscountEntity> discounts) {
        this.discounts = discounts;
    }
}
