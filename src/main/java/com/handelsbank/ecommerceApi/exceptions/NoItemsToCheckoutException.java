package com.handelsbank.ecommerceApi.exceptions;

public class NoItemsToCheckoutException extends RuntimeException {
    public NoItemsToCheckoutException(String message) {
        super(message);
    }
}
