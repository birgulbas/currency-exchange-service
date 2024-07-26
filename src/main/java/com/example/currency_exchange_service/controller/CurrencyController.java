package com.example.currency_exchange_service.controller;

import com.example.currency_exchange_service.DTO.CurrencyCreateDTO;
import com.example.currency_exchange_service.DTO.CurrencyDTO;
import com.example.currency_exchange_service.DTO.CurrencyFilterDTO;
import com.example.currency_exchange_service.DTO.CurrencyUpdateDTO;
import com.example.currency_exchange_service.api.CurrencyApi;
import com.example.currency_exchange_service.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController implements CurrencyApi {

    private final CurrencyService currencyService;


    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @GetMapping("/currencies") // tüm verileri listeler
    @Override
    public ResponseEntity<List<CurrencyCreateDTO>> getAllCurrency
            (@RequestParam(value = "deleted", required = false, defaultValue = "false") boolean deleted) {
        List<CurrencyCreateDTO> currencies = currencyService.getAllCurrency(deleted);
        return ResponseEntity.ok(currencies);
    }


    @PostMapping("/create")
    @Override
    public ResponseEntity<CurrencyCreateDTO> createCurrency(@Valid @RequestBody CurrencyCreateDTO currencyCreateDTO) { //valid size doğrulaması için

        currencyCreateDTO.normalize(); //normalizasyon işlemi
        CurrencyCreateDTO createdCurrency = currencyService.createCurrency(currencyCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCurrency);
    }


    @GetMapping("/currencyRate") //kurun türü ve tarihine göre arama yapar
    @Override
    public ResponseEntity<CurrencyDTO> getCurrencyByTypeAndDate(
            @RequestParam(required = true) String currencyType, //zorunlu alan
            @RequestParam(required = false) LocalDate currencyDate) { //zorunlu değil çünkü tarih girmezse otomatik tarih girelecek


        CurrencyDTO currencyDTO = currencyService.getCurrencyByTypeAndDate(currencyType, currencyDate);
        return ResponseEntity.ok(currencyDTO);
    }


    @PutMapping("/update") // Veriyi güncellemeyi sağlar
    @Override
    public ResponseEntity<CurrencyUpdateDTO> updateCurrency(@RequestBody CurrencyUpdateDTO currencyUpdateDTO) {
        currencyUpdateDTO.normalize(); //normalizasyon işlemi
        CurrencyUpdateDTO updatedCurrency = currencyService.updateCurrency(currencyUpdateDTO);
        return ResponseEntity.ok(updatedCurrency); // Güncellenen veriyi döndür
    }


    @GetMapping("/search")//filtreleyerek arama yapmayı sağlar
    public ResponseEntity<List<CurrencyFilterDTO>> getCurrenciesByTypeOrDateOrAll(
            @RequestParam(required = false) String currencyType, //zorunlu değil çünkü girmezse hepsi dönecek
            @RequestParam(required = false) LocalDate currencyDate) {

        List<CurrencyFilterDTO> filteredCurrencies = currencyService.getCurrenciesByTypeOrDateOrAll(currencyType, currencyDate);
        return ResponseEntity.ok(filteredCurrencies);
    }

    @PostMapping("/delete/{currencyType}/{currencyDate}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable String currencyType, @PathVariable LocalDate currencyDate) {

        currencyService.deleteCurrency(currencyType, currencyDate);
        return ResponseEntity.noContent().build();
    }


}
