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

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountDtoFactory accountDtoFactory;

    private static final String CREATE_ACCOUNT = "/api/accounts";
    private static final String DELETE_ACCOUNT = "/api/accounts/{account_id}";

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
    public AckDto deleteAccount(@PathVariable("account_id") Long id) {

        Optional<AccountEntity> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) throw new NotFoundException(String.format("Account with id \"%s\" is not found", id));

        accountRepository.delete(optionalAccount.get());

        return AckDto.makeDefault(true);
    }
}
