package com.znyar.simplepaysystem.store.repositories;

import com.znyar.simplepaysystem.store.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
}
