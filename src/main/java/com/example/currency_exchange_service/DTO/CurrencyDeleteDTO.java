package com.example.currency_exchange_service.DTO;

import com.example.currency_exchange_service.CustomSerializer.CustomLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyDeleteDTO {

    @Size(min = 2, max = 5)
    @JacksonXmlProperty(localName = "CurrencyCode")
    private String currencyCode;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @JacksonXmlProperty(localName = "Date")
    private LocalDate currencyDate;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDate getCurrencyDate() {
        return currencyDate;
    }

    public void setCurrencyDate(LocalDate currencyDate) {
        this.currencyDate = currencyDate;
    }

    public void normalize() { //küçük harfi büyük harfe normalize etmek için

        if (currencyCode != null) {
            currencyCode = currencyCode.trim().toUpperCase();

        }
    }
}


