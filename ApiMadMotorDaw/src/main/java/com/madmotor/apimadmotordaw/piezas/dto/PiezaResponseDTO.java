package com.madmotor.apimadmotordaw.piezas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PiezaResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String image;


}
