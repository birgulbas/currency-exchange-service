package com.example.currency_exchange_service.entity;

import com.example.currency_exchange_service.CustomSerializer.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class CompositeKey implements Serializable {

    @Size(min = 2, max = 5)
    @JacksonXmlProperty(localName = "CurrencyCode")
    private String currencyCode;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @JacksonXmlProperty(localName = "Date")
    private LocalDate currencyDate;

    public CompositeKey() {
    }

    public CompositeKey(String currencyCode, LocalDate currencyDate) {
        this.currencyCode = currencyCode;
        this.currencyDate = currencyDate;
    }

    public String getcurrencyCode() {
        return currencyCode;
    }

    public void setcurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }


    public LocalDate getCurrencyDate() {
        return currencyDate;
    }

    public void setCurrencyDate(LocalDate currencyDate) {
        this.currencyDate = currencyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
        return Objects.equals(currencyCode, that.currencyCode) && Objects.equals(currencyDate, that.currencyDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, currencyDate);
    }
}
