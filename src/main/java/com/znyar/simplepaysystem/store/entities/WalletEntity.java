package com.znyar.simplepaysystem.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(0)
    private BigDecimal cashBalance;
    @CreditCardNumber(ignoreNonDigitCharacters = true)
    @Column(unique = true)
    private String walletNumber;
    @ManyToOne
    private CurrencyEntity currency;
}
