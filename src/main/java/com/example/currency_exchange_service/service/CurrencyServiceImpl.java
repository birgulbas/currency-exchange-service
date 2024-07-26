package com.example.currency_exchange_service.service;

import com.example.currency_exchange_service.DTO.CurrencyCreateDTO;
import com.example.currency_exchange_service.DTO.CurrencyDTO;
import com.example.currency_exchange_service.DTO.CurrencyFilterDTO;
import com.example.currency_exchange_service.DTO.CurrencyUpdateDTO;
import com.example.currency_exchange_service.entity.CompositeKey;
import com.example.currency_exchange_service.entity.Currency;
import com.example.currency_exchange_service.exception.CurrencyAlreadyDeletedException;
import com.example.currency_exchange_service.exception.CurrencyAlreadyExistsException;
import com.example.currency_exchange_service.exception.CurrencyDeletedsuccessfullyException;
import com.example.currency_exchange_service.exception.CurrencyNotFoundException;
import com.example.currency_exchange_service.mapper.CurrencyMapper;
import com.example.currency_exchange_service.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger logger = Logger.getLogger(CurrencyServiceImpl.class.getName());

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }


    @Override
    public CurrencyCreateDTO createCurrency(CurrencyCreateDTO currencyCreateDTO) {
        Currency currency = currencyMapper.mapToCurrency(currencyCreateDTO);
        //composite key alanlarını atayabilmek için
        currency.setCurrencyType(currencyCreateDTO.getCurrencyType());
        currency.setCurrencyDate(currencyCreateDTO.getCurrencyDate());


        // Silinmemiş bir para birimi olup olmadığını kontrol et
        if (currencyRepository.existsByCurrencyTypeAndCurrencyDate(currency.getCurrencyType(), currency.getCurrencyDate())) {
            // Silinmiş bir para birimi olup olmadığını kontrol et
            Optional<Currency> existingDeletedCurrency = currencyRepository.findByCurrencyTypeAndDeletedTrue(currency.getCurrencyType());

            if (existingDeletedCurrency.isPresent()) {
                Currency deletedCurrency = existingDeletedCurrency.get();
                deletedCurrency.setDeleted(false); // Silinmiş durumunu kaldır
                deletedCurrency.setBuyingCurrencyRate(currency.getBuyingCurrencyRate());
                deletedCurrency.setSellingCurrencyRate(currency.getSellingCurrencyRate());

                Currency restoredCurrency = currencyRepository.save(deletedCurrency); // Silinmiş veriyi kaydet
                return currencyMapper.mapToCreateCurrencyDTO(restoredCurrency);
            } else {
                throw new CurrencyAlreadyExistsException("Sorry,Currency already exists..");
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
    public CurrencyDTO getCurrencyByTypeAndDate(String currencyType, LocalDate currencyDate) {
        CurrencyDTO currencyDTO = new CurrencyDTO();// CurrencyDTO'yu oluşturuyoruz ve gerekli bilgileri set ediyoruz
        currencyDTO.setCurrencyType(currencyType.toUpperCase()); //büyük harf olarak ara

        // Eğer tarih girilmediyse, en son tarihli veriyi al
        if (currencyDate == null) {
            List<Currency> latestCurrency = currencyRepository.findTopByCurrencyTypeOrderByCurrencyDateDesc(currencyType.toUpperCase());
            if (!latestCurrency.isEmpty()) {
                Currency currency = latestCurrency.get(0);
                currencyDate = currency.getCurrencyDate(); // En son tarihi al
            } else {
                throw new CurrencyNotFoundException("Sorry, the currency type you entered was not found.");
            }
        }


        currencyDTO.setCurrencyDate(currencyDate);
        List<Currency> optionalCurrency = currencyRepository.findByCurrencyTypeAndCurrencyDate(currencyType.toUpperCase(), currencyDate);

        // Eğer girilen değerdeki veri bulunuyorsa veriyi gösterir, bulunmuyorsa hata mesajı döndürür
        if (!optionalCurrency.isEmpty()) {
            Currency currency = optionalCurrency.get(0); // İlk elemanı alıyoruz
            currencyDTO.setBuyingCurrencyRate(currency.getBuyingCurrencyRate());
            currencyDTO.setSellingCurrencyRate(currency.getSellingCurrencyRate());
        } else {
            throw new CurrencyNotFoundException("Sorry, the currency type you entered was not found.");
        }


        return currencyDTO;
    }


    @Override
    public CurrencyUpdateDTO updateCurrency(CurrencyUpdateDTO currencyUpdateDTO) {
        //composite key tanımı
        CompositeKey key = new CompositeKey(currencyUpdateDTO.getCurrencyType(), currencyUpdateDTO.getCurrencyDate());
        String currencyType = currencyUpdateDTO.getCurrencyType();
        LocalDate currencyDate = currencyUpdateDTO.getCurrencyDate();

        List<Currency> currencies = currencyRepository.findByCurrencyTypeAndCurrencyDate(currencyType, currencyDate);

        if (!currencies.isEmpty()) {
            Currency currencyToUpdate = currencies.get(0);// Listeden güncellenecek ilk Currencyyi al
            currencyToUpdate.setBuyingCurrencyRate(currencyUpdateDTO.getBuyingCurrencyRate());
            currencyToUpdate.setSellingCurrencyRate(currencyUpdateDTO.getSellingCurrencyRate());

            Currency updatedCurrency = currencyRepository.save(currencyToUpdate);
            return currencyMapper.mapToUpdateCurrencyDTO(updatedCurrency);
        } else {
            throw new CurrencyNotFoundException("Sorry, the currency with type you entered that not found..");
        }
    }

    @Override
    public List<CurrencyFilterDTO> getCurrenciesByTypeOrDateOrAll(String currencyType, LocalDate currencyDate) {
        List<Currency> currencyFilterList;

        if (currencyType != null && currencyDate != null) { // Hem date hem type bilgisi sağlandıysa
            currencyFilterList = currencyRepository.findByCurrencyTypeAndCurrencyDate(currencyType.toUpperCase(), currencyDate);
        } else if (currencyType != null) { // Type girildiyse girilen tüm type değerlerini döndürür
            currencyFilterList = currencyRepository.findByCurrencyType(currencyType.toUpperCase());//veriyi büyük olarak arar
        } else if (currencyDate != null) { // Girilen o tarihteki tüm verileri döndürür
            currencyFilterList = currencyRepository.findByCurrencyDate(currencyDate);
        } else {
            currencyFilterList = currencyRepository.findAll(); // Date-type bilgisi girilmediyse hepsini döndürür.
        }

        return currencyFilterList.stream().map(currencyMapper::mapToFilterCurrencyDTO).collect(Collectors.toList());
    }


    @Override
    public void deleteCurrency(String currencyType, LocalDate currencyDate) {
        // Belirtilen type ve date ile eşleşen tüm currency kayıtlarını al
        List<Currency> currencies = currencyRepository.findByCurrencyTypeAndCurrencyDate(currencyType.toUpperCase(), currencyDate);

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
                throw new CurrencyDeletedsuccessfullyException("Delete process is succeeded.");
            } else {
                // Eğer bazı kayıtlar zaten silinmişse
                throw new CurrencyAlreadyDeletedException("Currency were already deleted.");
            }
        } else {
            throw new CurrencyNotFoundException("Currency not found.");
        }
    }
}
