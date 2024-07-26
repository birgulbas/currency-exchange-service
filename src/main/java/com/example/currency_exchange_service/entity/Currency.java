package com.example.currency_exchange_service.entity;

import com.example.currency_exchange_service.CustomBigDecimalSerializer.CustomBigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
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
public class Currency {
    @Id
    @Setter
    @Getter
    @Column(name = "currencyType", nullable = false)
    @Size(min = 2, max = 5)
    private String currencyType; //kurun cinsi

    @Id
    @Setter
    @Getter
    @Column(name = "currencyDate")
    private LocalDate currencyDate; //dövizin tarihi

    @Column(name = "buying", precision = 5, scale = 2)
    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal buyingCurrencyRate;

    @Column(name = "selling", precision = 5, scale = 2)
    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal sellingCurrencyRate;


    @Column(name = "deleted")
    private Boolean deleted = false; // Soft delete için otomatik olarak false döndürecek.

    public boolean deleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}







