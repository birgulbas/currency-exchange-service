package com.example.currency_exchange_service.DTO;

import com.example.currency_exchange_service.CustomSerializer.CustomLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TcmbYearCurrencyDTO {
    @Size(min = 2, max = 5)
    @JacksonXmlProperty(localName = "CurrencyCode")
    private String currencyCode;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @JacksonXmlProperty(localName = "Date")
    private LocalDate currencyDate;

    @JacksonXmlProperty(localName = "CurrencyName")
    private String currencyName;

    @JacksonXmlProperty(localName = "ForexBuying")
    private BigDecimal forexBuying;

    @JacksonXmlProperty(localName = "ForexSelling")
    private BigDecimal forexSelling;


    public TcmbYearCurrencyDTO() {
    }


}
