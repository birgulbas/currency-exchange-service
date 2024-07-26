package com.example.currency_exchange_service.DTO;

import com.example.currency_exchange_service.CustomBigDecimalSerializer.CustomBigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.time.LocalDate;


@XmlRootElement(name = "Currency")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class CurrencyDTO {

    @XmlElement(name = "CurrencyType")
    @Size(min = 2, max = 5)
    private String currencyType;

    @XmlElement(name = "currencyDate")
    private LocalDate currencyDate;

    @XmlElement(name = "buyingCurrencyRate")
    @JsonSerialize(using = CustomBigDecimalSerializer.class)//virgülden sonra girilen veriyi kısalmak için.
    private BigDecimal buyingCurrencyRate;

    @XmlElement(name = "sellingCurrencyRate")
    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal sellingCurrencyRate;

}


