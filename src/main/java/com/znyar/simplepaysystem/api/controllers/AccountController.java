package com.znyar.simplepaysystem.api.controllers;

import com.znyar.simplepaysystem.api.dto.AccountDto;
import com.znyar.simplepaysystem.api.dto.AckDto;
import com.znyar.simplepaysystem.api.exceptions.BadRequestException;
import com.znyar.simplepaysystem.api.exceptions.NotFoundException;
import com.znyar.simplepaysystem.api.factories.AccountDtoFactory;
import com.znyar.simplepaysystem.store.entities.AccountEntity;
import com.znyar.simplepaysystem.store.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountDtoFactory accountDtoFactory;

    private static final String CREATE_ACCOUNT = "/api/accounts";
    private static final String DELETE_ACCOUNT = "/api/accounts/{account_id}";
    private static final String GET_ACCOUNTS = "/api/accounts";
    private static final String GET_ACCOUNT = "/api/accounts/{account_id}";
    private static final String UPDATE_ACCOUNT = "/api/accounts/{account_id}";

    @PostMapping(CREATE_ACCOUNT)
    public AccountDto createAccount(@RequestParam("account_name") String accountName) {

        AccountEntity account = new AccountEntity();
        if (accountName.equals("")) {
            throw new BadRequestException("Account name must not be empty");
        }
        account.setName(accountName);
        return accountDtoFactory.makeAccountDto(accountRepository.save(account));
    }

    @DeleteMapping(DELETE_ACCOUNT)
    public AckDto deleteAccount(@PathVariable("account_id") Long accountId) {

        Optional<AccountEntity> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) {
            throw new NotFoundException(String.format("Account with id \"%s\" is not found", accountId));
        }

        accountRepository.delete(optionalAccount.get());

        return AckDto.makeDefault(true);
    }

    @GetMapping(GET_ACCOUNTS)
    public List<AccountDto> getAccounts() {
        return accountRepository.findAll().stream().map(accountDtoFactory::makeAccountDto).collect(Collectors.toList());
    }

    @GetMapping(GET_ACCOUNT)
    public AccountDto getAccount(@PathVariable("account_id") Long accountId) {

        Optional<AccountEntity> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isEmpty()) {
            throw new NotFoundException(String.format("Account with id \"%s\" is not found", accountId));
        }

        return accountDtoFactory.makeAccountDto(optionalAccount.get());
    }

    @PutMapping(UPDATE_ACCOUNT)
    public AccountDto updateAccount(
            @PathVariable("account_id") Long accountId,
            @RequestParam("account_name") String accountName
    ) {
        if (accountName.equals("")) {
            throw new BadRequestException("Account name must not be empty");
        }

        Optional<AccountEntity> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isEmpty()) {
            throw new NotFoundException(String.format("Account with id \"%s\" is not found", accountId));
        }

        AccountEntity account = optionalAccount.get();
        account.setName(accountName);

        return accountDtoFactory.makeAccountDto(accountRepository.save(account));
    }
}
