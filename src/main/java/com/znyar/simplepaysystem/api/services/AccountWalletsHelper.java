package com.znyar.simplepaysystem.api.services;

import com.znyar.simplepaysystem.api.exceptions.NotFoundException;
import com.znyar.simplepaysystem.store.entities.AccountEntity;
import com.znyar.simplepaysystem.store.entities.WalletEntity;
import com.znyar.simplepaysystem.store.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountWalletsHelper {

    private final AccountRepository accountRepository;

    public WalletEntity findAccountWallet(Long accountId, Long walletId) {
        Optional<AccountEntity> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) {
            throw new NotFoundException(String.format("Account with id \"%s\" is not found", accountId));
        }

        Optional<WalletEntity> optionalWallet = optionalAccount.get().getWallets()
                .stream()
                .filter(anotherWallet -> walletId.equals(anotherWallet.getId()))
                .findAny();

        if (optionalWallet.isEmpty()) {
            throw new NotFoundException(String.format("Wallet with id \"%s\" is not found", walletId));
        }

        return optionalWallet.get();
    }

}
