package com.example.currency_exchange_service.entity;

import com.example.currency_exchange_service.CustomSerializer.CustomBigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currency")
//nesne silinde hangi sql sorgusu olacagını belirtmek için
@SQLDelete(sql = "UPDATE currency SET deleted=true WHERE type = ?") //veriyi silmek yerine deleted sütununu true yapar
@IdClass(CompositeKey.class) // Bu sınıfa CompositeKey sınıfını belirtir
@JsonIgnoreProperties(ignoreUnknown = true)//var olmayan alanarı göz ardı etmek için
public class Currency {


    @Id
    @Column(name = "CurrencyCode", nullable = false)
    @Size(min = 2, max = 5)
    @JacksonXmlProperty(localName = "CurrencyCode")
    private String currencyCode; //kurun cinsi

    @Id
    @Column(name = "CurrencyDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    // @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @JacksonXmlProperty(localName = "Date")
    private LocalDate currencyDate; //dövizin tarihi

    @Column(name = "CurrencyName")
    @JacksonXmlProperty(localName = "CurrencyName")
    private String currencyName;

    @Column(name = "ForexBuying", precision = 5, scale = 2)
    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    @JacksonXmlProperty(localName = "ForexBuying")
    private BigDecimal forexBuying;

    @Column(name = "ForexSelling", precision = 5, scale = 2)
    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    @JacksonXmlProperty(localName = "ForexSelling")
    private BigDecimal forexSelling;


    @Column(name = "Deleted")
    private Boolean deleted = false; // Soft delete için otomatik olarak false döndürecek.

    public boolean deleted() {

        return deleted;
    }

    public void setDeleted(boolean deleted) {

        this.deleted = deleted;
    }

}







