package com.znyar.simplepaysystem.store.repositories;

import com.znyar.simplepaysystem.store.entities.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    Optional<CurrencyEntity> findByNameIgnoreCase(String name);

}
