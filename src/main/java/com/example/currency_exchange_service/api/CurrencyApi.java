package com.example.currency_exchange_service.api;

import com.example.currency_exchange_service.DTO.CurrencyCreateDTO;
import com.example.currency_exchange_service.DTO.CurrencyDTO;
import com.example.currency_exchange_service.DTO.CurrencyUpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyApi {


    @GetMapping("/currencies")
        // tüm verileri listeler
    ResponseEntity<List<CurrencyCreateDTO>> getAllCurrency
    (@RequestParam(value = "deleted", required = false, defaultValue = "false") boolean deleted);

    ResponseEntity<CurrencyCreateDTO> createCurrency(CurrencyCreateDTO currencyDTO);

    ResponseEntity<CurrencyDTO> getCurrencyByTypeAndDate(String currencyType, LocalDate currencyDate);

    ResponseEntity<CurrencyUpdateDTO> updateCurrency(CurrencyUpdateDTO currencyUpdateDTO);


}
