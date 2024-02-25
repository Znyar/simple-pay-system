package com.znyar.simplepaysystem.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Builder.Default
    private Instant createdAt = Instant.now();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<WalletEntity> wallets = new ArrayList<>();
}
