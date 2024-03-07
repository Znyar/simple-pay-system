package com.znyar.simplepaysystem.api.services;

import com.znyar.simplepaysystem.api.exceptions.InsufficientFundsException;
import com.znyar.simplepaysystem.api.exceptions.NotFoundException;
import com.znyar.simplepaysystem.store.entities.WalletEntity;
import com.znyar.simplepaysystem.store.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InnerTransferService {

    private final WalletRepository walletRepository;

    //TODO : add currency transfer
    @Transactional
    public void innerTransfer(WalletEntity senderWallet, String receiverWalletNumber, BigDecimal amount) {

        if (senderWallet.getCashBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        Optional<WalletEntity> optionalReceiverWallet = walletRepository.findByWalletNumber(receiverWalletNumber);

        if (optionalReceiverWallet.isEmpty()) {
            throw new NotFoundException(String.format("Wallet with id \"%s\" doesn`t exist", receiverWalletNumber));
        }

        WalletEntity receiverWallet = optionalReceiverWallet.get();

        receiverWallet.setCashBalance(receiverWallet.getCashBalance().add(amount));
        walletRepository.save(receiverWallet);

        senderWallet.setCashBalance(senderWallet.getCashBalance().subtract(amount));
        walletRepository.save(senderWallet);
    }

}
