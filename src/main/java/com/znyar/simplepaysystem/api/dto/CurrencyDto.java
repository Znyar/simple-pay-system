package com.znyar.simplepaysystem.api.dto;

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
public class CurrencyDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String bin;
    @NotNull
    private BigDecimal purchase;
    @NotNull
    private BigDecimal sale;
}
