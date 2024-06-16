package com.handelsbank.ecommerceApi.utilities;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class Validator<T> {

    public void validate(T object, Predicate<T> validationRule, RuntimeException exception) {
        if (!validationRule.test(object)) {
            throw exception;
        }
    }

    // Validates a list of objects
    public void validateList(List<T> objects, Predicate<T> validationRule, RuntimeException exception) {
        if (objects == null || objects.isEmpty()) {
            throw exception;
        }
        for (T object : objects) {
            validate(object, validationRule, exception);
        }
    }
}
