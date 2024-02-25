package com.znyar.simplepaysystem.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    @NotNull
    private Long id;
    @NotNull
    private BigDecimal cashBalance;
    @NotBlank
    private String walletNumber;
}
