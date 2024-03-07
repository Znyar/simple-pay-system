package com.znyar.simplepaysystem.api.services;

import com.znyar.simplepaysystem.api.dto.AckDto;
import com.znyar.simplepaysystem.api.exceptions.NotFoundException;
import com.znyar.simplepaysystem.api.services.other.WalletNumberGenerator;
import com.znyar.simplepaysystem.store.entities.CurrencyEntity;
import com.znyar.simplepaysystem.store.entities.WalletEntity;
import com.znyar.simplepaysystem.store.repositories.CurrencyRepository;
import com.znyar.simplepaysystem.store.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final CurrencyRepository currencyRepository;
    private final WalletNumberGenerator walletNumberGenerator;

    public WalletEntity createWallet(Optional<Long> optionalCurrencyId) {

        final WalletEntity wallet = new WalletEntity();

        if (optionalCurrencyId.isEmpty()) {
            Optional<CurrencyEntity> optionalCurrency = currencyRepository.findByNameIgnoreCase("RUB");
            optionalCurrency.ifPresentOrElse(
                    wallet::setCurrency,
                    () -> {
                        throw new NotFoundException("Default currency RUB doesn`t exist");
                    }
            );
        } else {
            Long currencyId = optionalCurrencyId.get();
            Optional<CurrencyEntity> optionalCurrency = currencyRepository.findById(currencyId);
            optionalCurrency.ifPresentOrElse(
                    wallet::setCurrency,
                    () -> {
                        throw new NotFoundException(
                                String.format("Currency with id \"%s\" doesn`t exist", currencyId)
                        );
                    }
            );
        }

        wallet.setWalletNumber(walletNumberGenerator.generate(wallet.getCurrency().getBin(), 16));
        wallet.setCashBalance(new BigDecimal(0));

        walletRepository.save(wallet);

        return wallet;
    }

}
