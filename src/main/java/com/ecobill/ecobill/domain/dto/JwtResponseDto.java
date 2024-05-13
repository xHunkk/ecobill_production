package com.ecobill.ecobill.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class JwtResponseDto {
    private String token;
    private Long id;
}
