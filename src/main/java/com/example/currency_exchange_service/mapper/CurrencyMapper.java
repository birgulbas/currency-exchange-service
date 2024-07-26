package com.example.currency_exchange_service.mapper;

import com.example.currency_exchange_service.DTO.CurrencyCreateDTO;
import com.example.currency_exchange_service.DTO.CurrencyFilterDTO;
import com.example.currency_exchange_service.DTO.CurrencyUpdateDTO;
import com.example.currency_exchange_service.entity.Currency;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {

    public CurrencyCreateDTO mapToCreateCurrencyDTO(Currency currency) {

        CurrencyCreateDTO currencyCreateDTO = new CurrencyCreateDTO();

        currencyCreateDTO.setCurrencyType(currency.getCurrencyType());
        currencyCreateDTO.setCurrencyDate(currency.getCurrencyDate());
        currencyCreateDTO.setBuyingCurrencyRate(currency.getBuyingCurrencyRate());
        currencyCreateDTO.setSellingCurrencyRate(currency.getSellingCurrencyRate());

        return currencyCreateDTO;
    }

    public Currency mapToCurrency(CurrencyCreateDTO currencyCreateDTO) {

        Currency currency = new Currency();
        currency.setCurrencyType(currencyCreateDTO.getCurrencyType());
        currency.setCurrencyDate(currencyCreateDTO.getCurrencyDate());
        currency.setBuyingCurrencyRate(currencyCreateDTO.getBuyingCurrencyRate());
        currency.setSellingCurrencyRate(currencyCreateDTO.getSellingCurrencyRate());

        return currency;
    }


    public CurrencyUpdateDTO mapToUpdateCurrencyDTO(Currency currency) {
        CurrencyUpdateDTO currencyUpdateDTO = new CurrencyUpdateDTO();

        currencyUpdateDTO.setCurrencyType(currency.getCurrencyType());
        currencyUpdateDTO.setCurrencyDate(currency.getCurrencyDate());
        currencyUpdateDTO.setBuyingCurrencyRate(currency.getBuyingCurrencyRate());
        currencyUpdateDTO.setSellingCurrencyRate(currency.getSellingCurrencyRate());

        return currencyUpdateDTO;
    }


    public CurrencyFilterDTO mapToFilterCurrencyDTO(Currency currency) {
        CurrencyFilterDTO currencyFilterDTO = new CurrencyFilterDTO();

        currencyFilterDTO.setCurrencyType(currency.getCurrencyType());
        currencyFilterDTO.setCurrencyDate(currency.getCurrencyDate());
        currencyFilterDTO.setBuyingCurrencyRate(currency.getBuyingCurrencyRate());
        currencyFilterDTO.setSellingCurrencyRate(currency.getSellingCurrencyRate());

        return currencyFilterDTO;
    }

}
