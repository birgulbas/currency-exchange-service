package com.example.currency_exchange_service.api;

import com.example.currency_exchange_service.DTO.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyApi {


    @GetMapping("/currencies")
    ResponseEntity<List<CurrencyCreateDTO>> getAllCurrency(@RequestParam(value = "deleted", required = false, defaultValue = "false") boolean deleted);

    @PostMapping("/create")
    ResponseEntity<CurrencyCreateDTO> createCurrency(@RequestBody CurrencyCreateDTO currencyDTO);

    @GetMapping("/currencyRate")
    ResponseEntity<CurrencyDTO> getCurrencyByCodeAndDate(
            @RequestParam String currencyCode,
            @RequestParam(required = false) LocalDate currencyDate);


    @PutMapping("/update")
    ResponseEntity<CurrencyUpdateDTO> updateCurrency(@RequestBody CurrencyUpdateDTO currencyUpdateDTO);

    @GetMapping("/search")
    ResponseEntity<List<CurrencyFilterDTO>> getCurrenciesByCodeOrDateOrAll(
            @RequestParam(required = false) String currencyCode,
            @RequestParam(required = false) LocalDate currencyDate);

    @PostMapping("/delete")
    ResponseEntity<Void> deleteCurrency(@RequestBody CurrencyDeleteDTO currencyDeleteDTO);


    @GetMapping("/tcmb/currencies")
    ResponseEntity<?> getTcmbCurrencies(@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date);

    @GetMapping("/getCurrenciesYear/{year}")
    ResponseEntity<List<TcmbYearCurrencyDTO>> getYearCurrenciesByTCMB(@PathVariable int year);

}
