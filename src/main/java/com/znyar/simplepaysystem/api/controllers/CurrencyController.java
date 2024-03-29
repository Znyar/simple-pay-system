package com.znyar.simplepaysystem.api.controllers;

import com.znyar.simplepaysystem.api.dto.AckDto;
import com.znyar.simplepaysystem.api.dto.CurrencyDto;
import com.znyar.simplepaysystem.api.exceptions.BadRequestException;
import com.znyar.simplepaysystem.api.exceptions.NotFoundException;
import com.znyar.simplepaysystem.api.factories.CurrencyDtoFactory;
import com.znyar.simplepaysystem.store.entities.CurrencyEntity;
import com.znyar.simplepaysystem.store.repositories.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyRepository currencyRepository;
    private final CurrencyDtoFactory currencyDtoFactory;

    private static final String CREATE_CURRENCY = "/api/currencies";
    private static final String DELETE_CURRENCY = "/api/currencies/{currency_id}";
    private static final String GET_CURRENCY_LIST = "/api/currencies";
    private static final String GET_CURRENCY = "/api/currencies/{currency_id}";
    private static final String UPDATE_CURRENCY = "/api/currencies/{currency_id}";

    @PostMapping(CREATE_CURRENCY)
    public CurrencyDto createCurrency(
            @RequestParam("currency_name") String currencyName,
            @RequestParam("currency_sale") BigDecimal currencySale,
            @RequestParam("currency_purchase") BigDecimal currencyPurchase,
            @RequestParam("currency_bin") String currencyBin
            ) {

        if (currencyRepository.findByNameIgnoreCase(currencyName).isPresent()) {
            throw new BadRequestException(String.format("Currency with name \"%s\" already exists", currencyName));
        }

        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .name(currencyName)
                .sale(currencySale)
                .purchase(currencyPurchase)
                .bin(currencyBin)
                .build();

        return currencyDtoFactory.makeCurrencyDto(currencyRepository.save(currencyEntity));
    }

    @Transactional
    @DeleteMapping(DELETE_CURRENCY)
    public AckDto deleteCurrency(@PathVariable("currency_id") Long currencyId) {

        if (currencyRepository.findById(currencyId).isEmpty()) {
            throw new NotFoundException(String.format("Currency with id \"%s\" doesn`t exist", currencyId));
        }

        currencyRepository.deleteById(currencyId);

        return AckDto.makeDefault(true);
    }

    @Transactional
    @GetMapping(GET_CURRENCY_LIST)
    public List<CurrencyDto> getCurrency() {
        return currencyRepository.findAll()
                .stream()
                .map(currencyDtoFactory::makeCurrencyDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @GetMapping(GET_CURRENCY)
    public CurrencyDto getCurrency(@PathVariable("currency_id") Long currencyId) {

        if (currencyRepository.findById(currencyId).isEmpty()) {
            throw new NotFoundException(String.format("Currency with id \"%s\" doesn`t exist", currencyId));
        }

        return currencyDtoFactory.makeCurrencyDto(currencyRepository.findById(currencyId).get());
    }

    @Transactional
    @PutMapping(UPDATE_CURRENCY)
    public CurrencyDto updateCurrency(
            @PathVariable("currency_id") Long currencyId,
            @RequestParam(value = "currency_sale", required = false) Optional<BigDecimal> currencySale,
            @RequestParam(value = "currency_purchase", required = false) Optional<BigDecimal> currencyPurchase,
            @RequestParam(value = "currency_name", required = false) Optional<String> currencyName,
            @RequestParam(value = "currency_bin", required = false) Optional<String> currencyBin
        ) {

        if (currencyRepository.findById(currencyId).isEmpty()) {
            throw new NotFoundException(String.format("Currency with id \"%s\" doesn`t exist", currencyId));
        }

        CurrencyEntity toUpdateCurrency = currencyRepository.findById(currencyId).get();

        if (currencyName.isPresent() && currencyRepository.findByNameIgnoreCase(currencyName.get()).isPresent()) {
            throw new BadRequestException(String.format("Currency with name \"%s\" already exist", currencyName.get()));
        }

        toUpdateCurrency.setName(currencyName.orElse(toUpdateCurrency.getName()));
        toUpdateCurrency.setBin(currencyBin.orElse(toUpdateCurrency.getBin()));
        toUpdateCurrency.setPurchase(currencyPurchase.orElse(toUpdateCurrency.getPurchase()));
        toUpdateCurrency.setSale(currencySale.orElse(toUpdateCurrency.getSale()));

        return currencyDtoFactory.makeCurrencyDto(currencyRepository.save(toUpdateCurrency));
    }

}
