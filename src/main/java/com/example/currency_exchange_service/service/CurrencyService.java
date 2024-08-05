package com.example.currency_exchange_service.service;

import com.example.currency_exchange_service.DTO.*;

import java.time.LocalDate;
import java.util.List;


public interface CurrencyService {

    CurrencyCreateDTO createCurrency(CurrencyCreateDTO currencyDTO);

    CurrencyDTO getCurrencyByCodeAndDate(String currencyCode, LocalDate currencyDate);

    CurrencyUpdateDTO updateCurrency(CurrencyUpdateDTO currencyUpdateDTO);

    List<CurrencyFilterDTO> getCurrenciesByCodeOrDateOrAll(String currencyCode, LocalDate currencyDate);

    void deleteCurrency(String currencyCode, LocalDate currencyDate);

    List<CurrencyCreateDTO> getAllCurrency(boolean deleted);

    List<TcmbCurrenciesDTO> getCurrenciesByTcmb(LocalDate date);

    List<TcmbYearCurrencyDTO> getAllCurrenciesForYear(int year);
}

