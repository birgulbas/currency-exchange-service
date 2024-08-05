package com.example.currency_exchange_service.controller;

import com.example.currency_exchange_service.DTO.*;
import com.example.currency_exchange_service.api.CurrencyApi;
import com.example.currency_exchange_service.exception.CurrencyNotFoundException;
import com.example.currency_exchange_service.response.ErrorResponse;
import com.example.currency_exchange_service.service.CurrencyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/currency")
public class CurrencyController implements CurrencyApi {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/create") //manuel veri ekleme
    public ResponseEntity<CurrencyCreateDTO> createCurrency(@Valid @RequestBody CurrencyCreateDTO currencyCreateDTO) {
        //valid size anatasyonunu doğrulaması için

        currencyCreateDTO.normalize();
        CurrencyCreateDTO createdCurrency = currencyService.createCurrency(currencyCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCurrency);
    }

    @GetMapping("/getCurrencies") // tüm verileri listeler
    @Override
    public ResponseEntity<List<CurrencyCreateDTO>> getAllCurrency
            (@RequestParam(value = "deleted", required = false, defaultValue = "false") boolean deleted) {
        List<CurrencyCreateDTO> currencies = currencyService.getAllCurrency(deleted);
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/search") //kurun türü ve tarihine göre arama yapar
    public ResponseEntity<CurrencyDTO> getCurrencyByCodeAndDate(@Valid
                                                                @RequestParam(required = true) String currencyCode, //zorunlu alan
                                                                @RequestParam(required = false) LocalDate currencyDate) { //zorunlu değil çünkü tarih girmezse otomatik tarih girelecek

        CurrencyDTO currencyDTO = currencyService.getCurrencyByCodeAndDate(currencyCode, currencyDate);
        return ResponseEntity.ok(currencyDTO);
    }

    @PutMapping("/update") // Veriyi güncellemeyi sağlar
    public ResponseEntity<CurrencyUpdateDTO> updateCurrency(@RequestBody CurrencyUpdateDTO currencyUpdateDTO) {
        currencyUpdateDTO.normalize(); //büyük harfe çevirme
        CurrencyUpdateDTO updatedCurrency = currencyService.updateCurrency(currencyUpdateDTO);
        return ResponseEntity.ok(updatedCurrency); // Güncellenen veriyi döndür
    }

    @GetMapping("/search/currencies")//filtreleyerek arama yapmayı sağlar
    public ResponseEntity<List<CurrencyFilterDTO>> getCurrenciesByCodeOrDateOrAll(
            @RequestParam(required = false) String currencyCode, //zorunlu değil çünkü girmezse hepsi dönecek
            @RequestParam(required = false) LocalDate currencyDate) {

        List<CurrencyFilterDTO> filteredCurrencies = currencyService.getCurrenciesByCodeOrDateOrAll(currencyCode, currencyDate);
        return ResponseEntity.ok(filteredCurrencies);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteCurrency(@RequestBody CurrencyDeleteDTO currencyDeleteDTO) {

        currencyService.deleteCurrency(currencyDeleteDTO.getCurrencyCode(), currencyDeleteDTO.getCurrencyDate());
        return ResponseEntity.noContent().build();

    }


    @GetMapping("/getCurrenciesByTcmb")
    public ResponseEntity<?> getTcmbCurrencies(@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        try {
            // Talep edilen tarihteki döviz verilerini al
            List<TcmbCurrenciesDTO> currencies = currencyService.getCurrenciesByTcmb(date);

            if (currencies != null && !currencies.isEmpty()) {
                return ResponseEntity.ok(currencies);
            } else { // Veri bulunamazsa, en yakın mevcut veriyi al
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Sorry, currency data not found for the requested date or previous 10 days.Please try a different date."));
            }
        } catch (CurrencyNotFoundException e) {
            System.err.println("CurrencyNotFoundException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred during the operation! Please try a different date."));
        }
    }


    @GetMapping("/getCurrenciesYear/{year}")
    public ResponseEntity<List<TcmbYearCurrencyDTO>> getYearCurrenciesByTCMB(@PathVariable int year) {
        try {
            List<TcmbYearCurrencyDTO> yearlyCurrencies = currencyService.getAllCurrenciesForYear(year);
            if (yearlyCurrencies.isEmpty()) {
                return ResponseEntity.noContent().build(); // İçerik yoksa 204 döner
            }
            return ResponseEntity.ok(yearlyCurrencies); // Başarılı yanıt
        } catch (Exception e) {
            System.out.println("Its failed: " + e.getMessage());
            return ResponseEntity.status(500).body(null); // Hata durumunda 500 döner
        }
    }
}



