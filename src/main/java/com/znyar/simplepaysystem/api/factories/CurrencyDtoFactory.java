package com.znyar.simplepaysystem.api.factories;

import com.znyar.simplepaysystem.api.dto.CurrencyDto;
import com.znyar.simplepaysystem.store.entities.CurrencyEntity;
import org.springframework.stereotype.Component;

@Component
public class CurrencyDtoFactory {
    public CurrencyDto makeCurrencyDto(CurrencyEntity currency) {
        return CurrencyDto.builder()
                .id(currency.getId())
                .name(currency.getName())
                .sale(currency.getSale())
                .purchase(currency.getPurchase())
                .build();
    }
}
