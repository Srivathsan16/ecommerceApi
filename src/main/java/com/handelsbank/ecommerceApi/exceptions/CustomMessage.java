package com.handelsbank.ecommerceApi.exceptions;

public enum CustomMessage {
    NO_ITEMS_TO_CHECKOUT("No items to checkout."),
    PRODUCT_NOT_FOUND("Product not found."),
    DATABASE_ERROR("Database operation failed.");
    // Add other custom messages as needed

    private final String message;

    CustomMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

