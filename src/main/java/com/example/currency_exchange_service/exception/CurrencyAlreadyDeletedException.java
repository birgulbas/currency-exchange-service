package com.example.currency_exchange_service.exception;

public class CurrencyAlreadyDeletedException extends RuntimeException {
    public CurrencyAlreadyDeletedException(String message) {
        super(message);
    }
}