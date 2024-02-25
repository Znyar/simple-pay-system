package com.znyar.simplepaysystem.store.repositories;

import com.znyar.simplepaysystem.store.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
