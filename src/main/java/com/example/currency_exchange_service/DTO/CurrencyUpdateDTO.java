package com.example.currency_exchange_service.DTO;

import com.example.currency_exchange_service.CustomBigDecimalSerializer.CustomBigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.time.LocalDate;


@XmlRootElement(name = "Currency")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class CurrencyUpdateDTO {

    @Size(min = 2, max = 5)
    private String currencyType;
    private LocalDate currencyDate;

    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal buyingCurrencyRate;

    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal sellingCurrencyRate;

    public void normalize() { //küçük harfi büyük harfe normalize etmek için

        if (currencyType != null) {
            currencyType = currencyType.trim().toUpperCase();

        }
    }


}
