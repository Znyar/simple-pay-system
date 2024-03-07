package com.znyar.simplepaysystem.api.controllers;

import com.znyar.simplepaysystem.api.dto.AckDto;
import com.znyar.simplepaysystem.api.dto.WalletDto;
import com.znyar.simplepaysystem.api.exceptions.BadRequestException;
import com.znyar.simplepaysystem.api.exceptions.NotFoundException;
import com.znyar.simplepaysystem.api.factories.WalletDtoFactory;
import com.znyar.simplepaysystem.api.services.AccountWalletsHelper;
import com.znyar.simplepaysystem.api.services.WalletService;
import com.znyar.simplepaysystem.store.entities.AccountEntity;
import com.znyar.simplepaysystem.store.entities.WalletEntity;
import com.znyar.simplepaysystem.store.repositories.AccountRepository;
import com.znyar.simplepaysystem.store.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class WalletController {

    private final WalletRepository walletRepository;
    private final AccountRepository accountRepository;
    private final WalletDtoFactory walletDtoFactory;
    private final WalletService walletService;
    private final AccountWalletsHelper accountWalletsHelper;

    private final static String CREATE_WALLET = "/api/accounts/{account_id}/wallets";
    private final static String DELETE_WALLET = "/api/accounts/{account_id}/wallets/{wallet_id}";
    private final static String GET_WALLETS = "/api/accounts/{account_id}/wallets";
    private final static String GET_WALLET = "/api/accounts/{account_id}/wallets/{wallet_id}";

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

    @Transactional
    @DeleteMapping(DELETE_WALLET)
    public AckDto deleteWallet(
            @PathVariable("account_id") Long accountId,
            @PathVariable("wallet_id") Long walletId
            ) {

        Optional<AccountEntity> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isEmpty()) {
            throw new NotFoundException(String.format("Account with id \"%s\" is not found", accountId));
        }

        Optional<WalletEntity> optionalWallet = walletRepository.findById(walletId);

        if (optionalWallet.isEmpty()) {
            throw new NotFoundException(String.format("Wallet with id \"%s\" doesn`t exist", walletId));
        }

        WalletEntity wallet = optionalWallet.get();

        if (wallet.getCashBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException("Cash balance is not zero");
        }

        AccountEntity account = optionalAccount.get();
        account.getWallets().remove(wallet);

        accountRepository.save(account);
        walletRepository.delete(wallet);

        return AckDto.makeDefault(true);
    }

    @GetMapping(GET_WALLETS)
    public List<WalletDto> getWallets(@PathVariable("account_id") Long accountId) {

        Optional<AccountEntity> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isEmpty()) {
            throw new NotFoundException(String.format("Account with id \"%s\" is not found", accountId));
        }

        return optionalAccount.get().getWallets()
                .stream()
                .map(walletDtoFactory::makeWalletDto)
                .collect(Collectors.toList());
    }

    @GetMapping(GET_WALLET)
    public WalletDto getWallet(
            @PathVariable("account_id") Long accountId,
            @PathVariable("wallet_id") Long walletId) {

        return walletDtoFactory.makeWalletDto(
                accountWalletsHelper.findAccountWallet(accountId, walletId));
    }
}
