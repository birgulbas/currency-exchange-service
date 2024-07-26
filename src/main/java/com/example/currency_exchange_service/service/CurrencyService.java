package com.example.currency_exchange_service.service;

import com.example.currency_exchange_service.DTO.CurrencyCreateDTO;
import com.example.currency_exchange_service.DTO.CurrencyDTO;
import com.example.currency_exchange_service.DTO.CurrencyFilterDTO;
import com.example.currency_exchange_service.DTO.CurrencyUpdateDTO;

import java.time.LocalDate;
import java.util.List;


public interface CurrencyService {

    CurrencyCreateDTO createCurrency(CurrencyCreateDTO currencyDTO);

    CurrencyDTO getCurrencyByTypeAndDate(String currencyType, LocalDate currencyDate);

    CurrencyUpdateDTO updateCurrency(CurrencyUpdateDTO currencyUpdateDTO);

    List<CurrencyFilterDTO> getCurrenciesByTypeOrDateOrAll(String currencyType, LocalDate currencyDate);

    void deleteCurrency(String currencyType, LocalDate currencyDate);

    List<CurrencyCreateDTO> getAllCurrency(boolean deleted);


}

