package com.example.currency_exchange_service.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class CurrencyCreateDTO {

    @Size(min = 2, max = 5)
    @XmlElement(name = "Currency_Type")
    private String currencyType;

    @XmlElement(name = "Currency_Date")
    private LocalDate currencyDate;

    @XmlElement(name = "Buying_Rate")
    private BigDecimal buyingCurrencyRate;

    @XmlElement(name = "Selling_Rate")
    private BigDecimal sellingCurrencyRate;

    public void normalize() {
        if (currencyType != null) {
            currencyType = currencyType.trim().toUpperCase();
        }
    }
}

