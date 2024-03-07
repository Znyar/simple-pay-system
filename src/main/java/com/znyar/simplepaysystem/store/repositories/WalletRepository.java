package com.znyar.simplepaysystem.store.repositories;

import com.znyar.simplepaysystem.store.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<WalletEntity, Long> {

    Optional<WalletEntity> findByWalletNumber(String number);

}
