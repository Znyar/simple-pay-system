package com.znyar.simplepaysystem.api.factories;

import com.znyar.simplepaysystem.api.dto.AccountDto;
import com.znyar.simplepaysystem.store.entities.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoFactory {

    public AccountDto makeAccountDto(AccountEntity account) {
        return AccountDto.builder()
                .id(account.getId())
                .name(account.getName())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
