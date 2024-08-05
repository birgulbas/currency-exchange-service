package com.example.currency_exchange_service.response;

import com.example.currency_exchange_service.CustomSerializer.CustomLocalDateDeserializer;
import com.example.currency_exchange_service.entity.Currency;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@JacksonXmlRootElement(localName = "CurrencyRates")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TcmbCurrencyResponse {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Currency")
    private List<Currency> currencies;


    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @JacksonXmlProperty(localName = "Date")
    private LocalDate currencyDate; //tarih bilgisi için

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public LocalDate getCurrencyDate() {
        return currencyDate;
    }


}

