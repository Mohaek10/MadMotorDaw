package com.madmotor.apimadmotordaw.piezas.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PiezaResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String image;


}
