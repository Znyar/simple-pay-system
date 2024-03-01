package com.znyar.simplepaysystem.api.controllers;

import com.znyar.simplepaysystem.api.dto.WalletDto;
import com.znyar.simplepaysystem.api.exceptions.NotFoundException;
import com.znyar.simplepaysystem.api.factories.WalletDtoFactory;
import com.znyar.simplepaysystem.api.services.WalletService;
import com.znyar.simplepaysystem.store.entities.AccountEntity;
import com.znyar.simplepaysystem.store.entities.CurrencyEntity;
import com.znyar.simplepaysystem.store.entities.WalletEntity;
import com.znyar.simplepaysystem.store.repositories.AccountRepository;
import com.znyar.simplepaysystem.store.repositories.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WalletController {

    private final AccountRepository accountRepository;
    private final WalletDtoFactory walletDtoFactory;
    private final WalletService walletService;

    private final static String CREATE_WALLET = "/api/accounts/{account_id}/wallets";

    @PostMapping(CREATE_WALLET)
    public WalletDto createWallet(
            @PathVariable("account_id") Long accountId,
            @RequestParam(value = "currency_id", required = false) Optional<Long> optionalCurrencyId
    ) {

        Optional<AccountEntity> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isEmpty()) {
            throw new NotFoundException(String.format("Account with id \"%s\" is not found", accountId));
        }

        AccountEntity account = optionalAccount.get();

        WalletEntity wallet = walletService.createWallet(optionalCurrencyId);

        List<WalletEntity> accountWallets = account.getWallets();
        accountWallets.add(wallet);
        account.setWallets(accountWallets);
        accountRepository.save(account);

        return walletDtoFactory.makeWalletDto(wallet);
    }
}
