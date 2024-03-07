package com.znyar.simplepaysystem.api.controllers;

import com.znyar.simplepaysystem.api.dto.AckDto;
import com.znyar.simplepaysystem.api.services.AccountWalletsHelper;
import com.znyar.simplepaysystem.api.dto.WalletDto;
import com.znyar.simplepaysystem.api.services.InnerTransferService;
import com.znyar.simplepaysystem.store.entities.WalletEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class InnerTransferController {

    private final InnerTransferService innerTransferService;
    private final AccountWalletsHelper accountWalletsHelper;

    private static final String INNER_TRANSFER = "/api/accounts/{account_id}/wallets/{wallet_id}/transfer";

    @PatchMapping(INNER_TRANSFER)
    public AckDto innerTransfer(
            @PathVariable("account_id") Long accountId,
            @PathVariable("wallet_id") Long walletId,
            @RequestParam("receiver_wallet_number") String receiverWalletNumber,
            @RequestParam("amount") BigDecimal amount
            ) {

        WalletEntity wallet = accountWalletsHelper.findAccountWallet(accountId, walletId);

        innerTransferService.innerTransfer(wallet, receiverWalletNumber, amount);

        return AckDto.makeDefault(true);
    }

}
