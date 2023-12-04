package com.madmotor.apimadmotordaw.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "JwtAuthResponse", description = "JwtAuthResponse")
public class JwtAuthResponse {
    @Schema(description = "Token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYWRtb3RvciIsImV4cCI6")
    private String token;
}