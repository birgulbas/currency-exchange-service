package com.example.currency_exchange_service.mapper;

import com.example.currency_exchange_service.DTO.*;
import com.example.currency_exchange_service.entity.Currency;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {

    public CurrencyCreateDTO mapToCreateCurrencyDTO(Currency currency) {

        CurrencyCreateDTO currencyCreateDTO = new CurrencyCreateDTO();

        currencyCreateDTO.setCurrencyCode(currency.getCurrencyCode());
        currencyCreateDTO.setCurrencyDate(currency.getCurrencyDate());
        currencyCreateDTO.setCurrencyName(currency.getCurrencyName());
        currencyCreateDTO.setForexBuying(currency.getForexBuying());
        currencyCreateDTO.setForexSelling(currency.getForexSelling());

        return currencyCreateDTO;
    }

    public Currency mapToCurrency(CurrencyCreateDTO currencyCreateDTO) {

        Currency currency = new Currency();
        currency.setCurrencyCode(currencyCreateDTO.getCurrencyCode());
        currency.setCurrencyDate(currencyCreateDTO.getCurrencyDate());
        currency.setCurrencyName(currencyCreateDTO.getCurrencyName());
        currency.setForexBuying(currencyCreateDTO.getForexBuying());
        currency.setForexSelling(currencyCreateDTO.getForexSelling());

        return currency;
    }


    public CurrencyUpdateDTO mapToUpdateCurrencyDTO(Currency currency) {
        CurrencyUpdateDTO currencyUpdateDTO = new CurrencyUpdateDTO();

        currencyUpdateDTO.setCurrencyCode(currency.getCurrencyCode());
        currencyUpdateDTO.setCurrencyDate(currency.getCurrencyDate());
        currencyUpdateDTO.setCurrencyName(currency.getCurrencyName());
        currencyUpdateDTO.setForexBuying(currency.getForexBuying());
        currencyUpdateDTO.setForexSelling(currency.getForexSelling());

        return currencyUpdateDTO;
    }


    public CurrencyFilterDTO mapToFilterCurrencyDTO(Currency currency) {
        CurrencyFilterDTO currencyFilterDTO = new CurrencyFilterDTO();


        currencyFilterDTO.setCurrencyCode(currency.getCurrencyCode());
        currencyFilterDTO.setCurrencyDate(currency.getCurrencyDate());
        currencyFilterDTO.setCurrencyName(currency.getCurrencyName());
        currencyFilterDTO.setForexBuying(currency.getForexBuying());
        currencyFilterDTO.setForexSelling(currency.getForexSelling());

        return currencyFilterDTO;
    }


    public TcmbCurrenciesDTO mapToTcmbCurrenciesDTO(Currency currency) {
        TcmbCurrenciesDTO tcmbCurrenciesDTO = new TcmbCurrenciesDTO();

        tcmbCurrenciesDTO.setCurrencyCode(currency.getCurrencyCode());
        tcmbCurrenciesDTO.setCurrencyDate(currency.getCurrencyDate());
        tcmbCurrenciesDTO.setCurrencyName(currency.getCurrencyName());
        tcmbCurrenciesDTO.setForexBuying(currency.getForexBuying());
        tcmbCurrenciesDTO.setForexSelling(currency.getForexSelling());


        return tcmbCurrenciesDTO;
    }


    public Currency mapToCurrencyTcmb(TcmbCurrenciesDTO tcmbCurrenciesDTO) {
        Currency currency = new Currency();

        currency.setCurrencyCode(tcmbCurrenciesDTO.getCurrencyCode());
        currency.setCurrencyDate(tcmbCurrenciesDTO.getCurrencyDate());
        currency.setCurrencyName(tcmbCurrenciesDTO.getCurrencyName());
        currency.setForexBuying(tcmbCurrenciesDTO.getForexBuying());
        currency.setForexSelling(tcmbCurrenciesDTO.getForexSelling());
        return currency;
    }


    public TcmbYearCurrencyDTO mapToTcmbYearCurrencyDTO(Currency currency) {

        TcmbYearCurrencyDTO tcmbYearCurrencyDTO = new TcmbYearCurrencyDTO();

        tcmbYearCurrencyDTO.setCurrencyCode(currency.getCurrencyCode());
        tcmbYearCurrencyDTO.setCurrencyDate(currency.getCurrencyDate());
        tcmbYearCurrencyDTO.setCurrencyName(currency.getCurrencyName());
        tcmbYearCurrencyDTO.setForexBuying(currency.getForexBuying());
        tcmbYearCurrencyDTO.setForexSelling(currency.getForexSelling());

        return tcmbYearCurrencyDTO;
    }


    public Currency mapToCurrencyTcmbYear(TcmbYearCurrencyDTO tcmbYearCurrencyDTO) {

        Currency currency = new Currency();
        currency.setCurrencyCode(tcmbYearCurrencyDTO.getCurrencyCode());
        currency.setCurrencyDate(tcmbYearCurrencyDTO.getCurrencyDate());
        currency.setCurrencyName(tcmbYearCurrencyDTO.getCurrencyName());
        currency.setForexBuying(tcmbYearCurrencyDTO.getForexBuying());
        currency.setForexSelling(tcmbYearCurrencyDTO.getForexSelling());
        return currency;
    }
}
