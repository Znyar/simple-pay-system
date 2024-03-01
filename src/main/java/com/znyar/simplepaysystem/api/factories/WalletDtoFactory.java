package com.znyar.simplepaysystem.api.factories;

import com.znyar.simplepaysystem.api.dto.WalletDto;
import com.znyar.simplepaysystem.store.entities.WalletEntity;
import org.springframework.stereotype.Component;

@Component
public class WalletDtoFactory {
    public WalletDto makeWalletDto(WalletEntity wallet) {
        return WalletDto.builder()
                .id(wallet.getId())
                .walletNumber(wallet.getWalletNumber())
                .cashBalance(wallet.getCashBalance())
                .currencyName(wallet.getCurrency().getName())
                .build();
    }
}
