package com.znyar.simplepaysystem.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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
    @Size(max = 4, min = 4)
    @Column(unique = true)
    private String bin;
    @Min(0)
    private BigDecimal purchase;
    @Min(0)
    private BigDecimal sale;
}
