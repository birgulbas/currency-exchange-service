package com.example.currency_exchange_service.repository;

import com.example.currency_exchange_service.entity.CompositeKey;
import com.example.currency_exchange_service.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface CurrencyRepository extends JpaRepository<Currency, CompositeKey> {

    List<Currency> findByCurrencyCodeAndCurrencyDate(String currencyType, LocalDate currencyDate);

    List<Currency> findByCurrencyCode(String currencyDate);

    List<Currency> findByCurrencyDate(LocalDate currencyDate);

    boolean existsByCurrencyCodeAndCurrencyDate(String currencyType, LocalDate currencyDate);

    @Query("SELECT c FROM Currency c WHERE c.currencyCode = :currencyType AND c.deleted = false ORDER BY c.currencyDate DESC")
    List<Currency> findTopByCurrencyCodeOrderByCurrencyDateDesc(@Param("currencyType") String currencyType);

    @Query("SELECT c FROM Currency c WHERE c.deleted = false")
    List<Currency> findByDeletedFalse();

    Optional<Currency> findByCurrencyCodeAndDeletedTrue(String currencyType);


}




