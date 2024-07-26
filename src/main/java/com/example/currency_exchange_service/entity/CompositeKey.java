package com.example.currency_exchange_service.entity;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class CompositeKey implements Serializable {

    @Size(min = 2, max = 5)
    private String currencyType;
    private LocalDate currencyDate;

    public CompositeKey() {
    }

    public CompositeKey(String currencyType, LocalDate currencyDate) {
        this.currencyType = currencyType;
        this.currencyDate = currencyDate;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
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
        return Objects.equals(currencyType, that.currencyType) && Objects.equals(currencyDate, that.currencyDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyType, currencyDate);
    }
}
