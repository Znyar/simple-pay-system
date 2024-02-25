package com.znyar.simplepaysystem.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @JsonProperty("created_at")
    private Instant createdAt = Instant.now();
}
