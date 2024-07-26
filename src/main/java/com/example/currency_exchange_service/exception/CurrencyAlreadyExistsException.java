package com.example.currency_exchange_service.exception;

public class CurrencyAlreadyExistsException extends RuntimeException {

    public CurrencyAlreadyExistsException(String message) {

        super(message); //exceptiona mesaj parametresini iletir.
    }
}
