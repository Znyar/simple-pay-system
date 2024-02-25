package com.znyar.simplepaysystem.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "currency")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CurrencyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 3)
    private String name;
    @Min(0)
    private BigDecimal purchase;
    @Min(0)
    private BigDecimal sale;
}
