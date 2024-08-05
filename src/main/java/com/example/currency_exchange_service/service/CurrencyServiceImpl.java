package com.example.currency_exchange_service.service;

import com.example.currency_exchange_service.DTO.*;
import com.example.currency_exchange_service.entity.CompositeKey;
import com.example.currency_exchange_service.entity.Currency;
import com.example.currency_exchange_service.exception.CurrencyAlreadyDeletedException;
import com.example.currency_exchange_service.exception.CurrencyAlreadyExistsException;
import com.example.currency_exchange_service.exception.CurrencyDeletedsuccessfullyException;
import com.example.currency_exchange_service.exception.CurrencyNotFoundException;
import com.example.currency_exchange_service.mapper.CurrencyMapper;
import com.example.currency_exchange_service.repository.CurrencyRepository;
import com.example.currency_exchange_service.response.TcmbCurrencyResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger logger = Logger.getLogger(CurrencyServiceImpl.class.getName());

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final WebClient webClient;


    @Value("${api.base.url}") // Konfigürasyon dosyasındaki property'yi alır
    private String apiBaseUrl;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper, WebClient.Builder webClientBuilder) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
        this.webClient = webClientBuilder.baseUrl(apiBaseUrl).build();
    }

    @Override
    public CurrencyCreateDTO createCurrency(CurrencyCreateDTO currencyCreateDTO) {
        Currency currency = currencyMapper.mapToCurrency(currencyCreateDTO);
        //composite key alanlarını atayabilmek için
        currency.setCurrencyCode(currencyCreateDTO.getCurrencyCode());
        currency.setCurrencyDate(currencyCreateDTO.getCurrencyDate());

        // Silinmemiş bir para birimi olup olmadığını kontrol et
        if (currencyRepository.existsByCurrencyCodeAndCurrencyDate(currency.getCurrencyCode(), currency.getCurrencyDate())) {
            // Silinmiş bir para birimi olup olmadığını kontrol et
            Optional<Currency> existingDeletedCurrency = currencyRepository.findByCurrencyCodeAndDeletedTrue(currency.getCurrencyCode());

            if (existingDeletedCurrency.isPresent()) {
                Currency deletedCurrency = existingDeletedCurrency.get();
                deletedCurrency.setDeleted(false); // Silinmiş durumunu kaldır
                deletedCurrency.setForexBuying(currency.getForexBuying());
                deletedCurrency.setForexSelling(currency.getForexSelling());

                Currency restoredCurrency = currencyRepository.save(deletedCurrency); // Silinmiş veriyi kaydet
                return currencyMapper.mapToCreateCurrencyDTO(restoredCurrency);
            } else {
                throw new CurrencyAlreadyExistsException("Currency data already exists..");
            }

        } else {
            Currency savedCurrency = currencyRepository.save(currency);
            return currencyMapper.mapToCreateCurrencyDTO(savedCurrency);
        }
    }

    public List<CurrencyCreateDTO> getAllCurrency(boolean deleted) {

        List<Currency> currencyList = currencyRepository.findByDeletedFalse(); // Silinmemiş kayıtları al

        return currencyList.stream()
                .map(currencyMapper::mapToCreateCurrencyDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CurrencyDTO getCurrencyByCodeAndDate(String currencyCode, LocalDate currencyDate) {
        CurrencyDTO currencyDTO = new CurrencyDTO();// CurrencyDTO'yu oluşturuyoruz ve gerekli bilgileri set ediyoruz
        currencyDTO.setCurrencyCode(currencyCode.toUpperCase()); //büyük harf olarak ara

        // Eğer tarih girilmediyse, en son tarihli veriyi al
        if (currencyDate == null) {
            List<Currency> latestCurrency = currencyRepository.findTopByCurrencyCodeOrderByCurrencyDateDesc(currencyCode.toUpperCase());
            if (!latestCurrency.isEmpty()) {
                Currency currency = latestCurrency.get(0);
                currencyDate = currency.getCurrencyDate(); // En son tarihi al
            } else {
                throw new CurrencyNotFoundException("Sorry, the currency type data that you entered was not found.");
            }
        }

        currencyDTO.setCurrencyDate(currencyDate);
        List<Currency> optionalCurrency = currencyRepository.findByCurrencyCodeAndCurrencyDate(currencyCode.toUpperCase(), currencyDate);

        // Eğer girilen değerdeki veri bulunuyorsa veriyi gösterir, bulunmuyorsa hata mesajı döndürür
        if (!optionalCurrency.isEmpty()) {
            Currency currency = optionalCurrency.get(0); // İlk elemanı alıyoruz
            currencyDTO.setCurrencyName(currency.getCurrencyName());
            currencyDTO.setForexBuying(currency.getForexBuying());
            currencyDTO.setForexSelling(currency.getForexSelling());
        } else {
            throw new CurrencyNotFoundException("Sorry, the currency type data that you entered was not found.");
        }
        return currencyDTO;
    }

    @Override
    public CurrencyUpdateDTO updateCurrency(CurrencyUpdateDTO currencyUpdateDTO) {

        //composite key tanımı
        CompositeKey key = new CompositeKey(currencyUpdateDTO.getCurrencyCode(), currencyUpdateDTO.getCurrencyDate());
        String currencyType = currencyUpdateDTO.getCurrencyCode();
        LocalDate currencyDate = currencyUpdateDTO.getCurrencyDate();

        List<Currency> currencies = currencyRepository.findByCurrencyCodeAndCurrencyDate(currencyType, currencyDate);

        if (!currencies.isEmpty()) {
            Currency currencyToUpdate = currencies.get(0);// Listeden güncellenecek ilk Currencyyi al
            currencyToUpdate.setForexBuying(currencyUpdateDTO.getForexBuying());
            currencyToUpdate.setForexSelling(currencyUpdateDTO.getForexSelling());

            Currency updatedCurrency = currencyRepository.save(currencyToUpdate);
            return currencyMapper.mapToUpdateCurrencyDTO(updatedCurrency);
        } else {
            throw new CurrencyNotFoundException("Sorry, the currency data that you entered that not found..");
        }
    }

    @Override
    public List<CurrencyFilterDTO> getCurrenciesByCodeOrDateOrAll(String currencyCode, LocalDate currencyDate) {
        List<Currency> currencyFilterList;

        if (currencyCode != null && currencyDate != null) { // Hem date hem type bilgisi sağlandıysa
            currencyFilterList = currencyRepository.findByCurrencyCodeAndCurrencyDate(currencyCode.toUpperCase(), currencyDate);
        } else if (currencyCode != null) { // Type girildiyse girilen tüm type değerlerini döndürür
            currencyFilterList = currencyRepository.findByCurrencyCode(currencyCode.toUpperCase());
            if (currencyFilterList.isEmpty()) {
                // Boş liste dönerse, hata yerine tüm verileri döndür
                logger.info("No data found for the specified currency code. Returning all available data.");
                currencyFilterList = currencyRepository.findAll();

            }
        } else if (currencyDate != null) { // Girilen o tarihteki tüm verileri döndürür
            currencyFilterList = currencyRepository.findByCurrencyDate(currencyDate);
            if (currencyFilterList.isEmpty()) {
                logger.info("No data found for the specified currency code. Returning all available data.");
                // Boş liste dönerse, hata yerine tüm verileri döndür
                currencyFilterList = currencyRepository.findAll();
            }
        } else { // Hiçbir parametre verilmemişse
            logger.info("No data found. Returning all available data.");
            currencyFilterList = currencyRepository.findAll();
        }

        return currencyFilterList.stream()
                .map(currencyMapper::mapToFilterCurrencyDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteCurrency(String currencyCode, LocalDate currencyDate) {
        // Belirtilen type ve date ile eşleşen tüm currency kayıtlarını al
        List<Currency> currencies = currencyRepository.findByCurrencyCodeAndCurrencyDate(currencyCode.toUpperCase(), currencyDate);

        if (!currencies.isEmpty()) {
            boolean allDeleted = true;

            for (Currency currency : currencies) {
                if (!currency.getDeleted()) {
                    currency.setDeleted(true); // delete işlemi
                    currencyRepository.save(currency); // Güncellenmiş kayıtları kaydediyoruz
                } else {
                    // Eğer kayıt zaten silindiyse, tüm kayıtların silinip silinmediğini kontrol eder
                    allDeleted = false;
                }
            }

            if (allDeleted) {
                // Eğer tüm kayıtlar başarıyla silindiyse
                throw new CurrencyDeletedsuccessfullyException("Delete process is succeeded!");
            } else {
                // Eğer bazı kayıtlar zaten silinmişse
                throw new CurrencyAlreadyDeletedException("Currency data were already deleted.");
            }
        } else {
            throw new CurrencyNotFoundException("Sorry, Currency data that you entered not found.");
        }

    }


    @Override
    public List<TcmbCurrenciesDTO> getCurrenciesByTcmb(LocalDate date) {
        LocalDate actualDate = getCurrencyDataWithRetry(date);

        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            String formattedDate = actualDate.format(dateFormatter);
            String monthYear = actualDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
            String url = String.format("%s/%s/%s.xml", apiBaseUrl, monthYear, formattedDate);

            String apiResponse = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.registerModule(new JavaTimeModule());

            // XML'yi Java'ya dönüştür
            TcmbCurrencyResponse currencyResponse = xmlMapper.readValue(apiResponse, TcmbCurrencyResponse.class);

            // Yanıtı doğrulama
            if (currencyResponse == null || currencyResponse.getCurrencies() == null || currencyResponse.getCurrencies().isEmpty()) {
                throw new CurrencyNotFoundException("Sorry, currency data not found from the API URL.");
            }

            // DTO'ya dönüştürme
            List<TcmbCurrenciesDTO> saveCurrencies = currencyResponse.getCurrencies().stream()
                    .map(currencyMapper::mapToTcmbCurrenciesDTO) // Dönüşüm işlemi
                    .collect(Collectors.toList());
            saveCurrencies.forEach(currency -> currency.setCurrencyDate(actualDate)); //girilen tarihi set eder

            List<Currency> saveCurrenciesEntities = saveCurrencies.stream()
                    .map(currencyMapper::mapToCurrencyTcmb) // DTO'dan entity'ye dönüşüm
                    .collect(Collectors.toList());

            // Entity listesini kaydetme
            currencyRepository.saveAll(saveCurrenciesEntities);

            // Mesaj yazdır
            System.out.println("Data retrieved for date: " + actualDate); //girilen tarihin çıktısı

            return saveCurrencies; // Kaydedilen DTO'ları döndür

        } catch (Exception e) {
            throw new CurrencyNotFoundException("Sorry, Failed to process currency rates.");
        }
    }

    private LocalDate getCurrencyDataWithRetry(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        LocalDate currentDate = date;

        for (int i = 0; i < 30; i++) {
            String formattedDate = currentDate.format(dateFormatter);
            String monthYear = currentDate.format(monthYearFormatter);
            String url = String.format("%s/%s/%s.xml", apiBaseUrl, monthYear, formattedDate);

            try {
                String apiResponse = webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                // API yanıtını kontrol et
                if (apiResponse != null && !apiResponse.isEmpty()) {
                    return currentDate;
                }
            } catch (Exception e) {
                // Hata durumunda, gerekirse hata loglama
            }

            // Bir gün geri git
            currentDate = currentDate.minusDays(1);
        }

        throw new CurrencyNotFoundException("No currency data available for the given date or previous 30 days.");
    }


    public List<TcmbYearCurrencyDTO> getAllCurrenciesForYear(int year) {
        List<TcmbYearCurrencyDTO> allDailyCurrencies = new ArrayList<>();

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("yyyyMM");

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate currentDate = date;
            String formattedDate = date.format(dateFormatter);
            String monthYear = date.format(monthYearFormatter);
            String url = String.format("%s/%s/%s.xml", apiBaseUrl, monthYear, formattedDate);

            try {
                String apiResponse = webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                if (apiResponse != null && !apiResponse.isEmpty()) {
                    XmlMapper xmlMapper = new XmlMapper();
                    xmlMapper.registerModule(new JavaTimeModule());

                    TcmbCurrencyResponse currencyResponse = xmlMapper.readValue(apiResponse, TcmbCurrencyResponse.class);

                    if (currencyResponse == null || currencyResponse.getCurrencies() == null || currencyResponse.getCurrencies().isEmpty()) {
                        System.out.printf("%s: No currency data found. Moving to the next day.%n", currentDate);
                        continue; // Veri yoksa bir sonraki güne geç
                    }

                    List<TcmbYearCurrencyDTO> saveDailyCurrencies = currencyResponse.getCurrencies().stream()
                            .map(currencyMapper::mapToTcmbYearCurrencyDTO)
                            .collect(Collectors.toList());
                    saveDailyCurrencies.forEach(currency -> currency.setCurrencyDate(currentDate));

                    List<Currency> saveDailyCurrenciesEntities = saveDailyCurrencies.stream()
                            .map(currencyMapper::mapToCurrencyTcmbYear)
                            .collect(Collectors.toList());

                    currencyRepository.saveAll(saveDailyCurrenciesEntities);

                    allDailyCurrencies.addAll(saveDailyCurrencies);
                } else {
                    System.out.println("API response is empty.");
                }
            } catch (Exception e) {
                System.out.printf("Failed to process currency rates for %s. Skipping the day.%n", currentDate);
            }
        }

        return allDailyCurrencies;
    }
}










