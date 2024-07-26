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

    List<Currency> findByCurrencyTypeAndCurrencyDate(String currencyType, LocalDate currencyDate);

    List<Currency> findByCurrencyType(String currencyDate);

    List<Currency> findByCurrencyDate(LocalDate currencyDate);

    boolean existsByCurrencyTypeAndCurrencyDate(String currencyType, LocalDate currencyDate);

    @Query("SELECT c FROM Currency c WHERE c.currencyType = :currencyType AND c.deleted = false")
    List<Currency> findTopByCurrencyTypeOrderByCurrencyDateDesc(@Param("currencyType") String currencyType);

    @Query("SELECT c FROM Currency c WHERE c.deleted = false")
    List<Currency> findByDeletedFalse();

    Optional<Currency> findByCurrencyTypeAndDeletedTrue(String currencyType);

}
