package com.example.currency_exchange_service.DTO;

import com.example.currency_exchange_service.CustomSerializer.CustomLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)//var olmayan alanarı göz ardı etmek için
public class CurrencyCreateDTO {

    @Size(min = 2, max = 5)
    @Pattern(regexp = "[a-zA-Z]{2,5}", message = "Currency code must be a string with 2 to 5 letters")
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

    public void normalize() {
        if (currencyCode != null) {
            currencyCode = currencyCode.trim().toUpperCase();
        }
    }


}

