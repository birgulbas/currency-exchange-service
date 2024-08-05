package com.example.currency_exchange_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CurrencyAlreadyExistsException.class)
    public ResponseEntity<String> handleCurrencyAlreadyExists(CurrencyAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<String> handleCurrencyNotFound(CurrencyNotFoundException ex)//istisna nesnesi
    {
        //hata mesajını döndürür
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CurrencyDeletedsuccessfullyException.class)
    public ResponseEntity<String> handleCurrencyDeletedsuccessfullyException(CurrencyDeletedsuccessfullyException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errorResponse = new HashMap<>(); //hashmap nesnesi ile başlat //doğrulama hatalarını saklamak için map oluşturur
        errorResponse.put("error", " Your value is must be string and between 2-5 character!");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }//Hata haritasını 400 Bad Request HTTP durumu koduyla döner.


    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



