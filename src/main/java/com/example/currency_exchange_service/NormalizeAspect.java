package com.example.currency_exchange_service;

import com.example.currency_exchange_service.DTO.CurrencyCreateDTO;
import com.example.currency_exchange_service.DTO.CurrencyUpdateDTO;
import org.aspectj.lang.annotation.Before;

public class NormalizeAspect {

    @Before("execution(* com.example.currency_exchange_service..*(..)) && args(currencyUpdateDTO,..)")
    public void normalizeCurrencyUpdateDTO(CurrencyUpdateDTO currencyUpdateDTO) {
        if (currencyUpdateDTO != null) {
            currencyUpdateDTO.normalize();
        }
    }

    @Before("execution(* com.example.currency_exchange_service..*(..)) && args(CurrencyCreateDTO,..)")
    public void normalizeCurrencyTCreateDTO(CurrencyCreateDTO currencyCreateDTO) {
        if (currencyCreateDTO != null) {
            currencyCreateDTO.normalize();
        }
    }


}

