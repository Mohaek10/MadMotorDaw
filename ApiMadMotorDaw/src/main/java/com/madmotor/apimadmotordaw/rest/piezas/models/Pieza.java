package com.madmotor.apimadmotordaw.rest.piezas.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase Pieza
 *
 * En esta clase se definen los atributos de la clase Pieza
 * @version 1.0
 * @Author Rubén Fernández
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "PIEZAS")
@EntityListeners(AuditingEntityListener.class)

public class Pieza {
    @Schema(description = "Imagen de la pieza predeterminada")
    public static final String IMAGE_DEFAULT = "https://via.placeholder.com/150";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador de la pieza")
    private UUID id;


    @Column(name = "nombre", nullable = false, length = 50)
    @NotBlank
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(description = "Nombre de la pieza")
    private String name;

    @Length(min = 3, max = 200, message = "La descripción debe tener entre 3 y 200 caracteres")
    @Column(name = "descripcion", nullable = false)
    @Schema(description = "Descripción de la pieza")
    private String description;

    @Positive(message = "El precio debe ser mayor que 0")
    @Column(name = "precio", nullable = false )
    @Schema(description = "Precio de la pieza")
    private Double price;

    @Column
    @PositiveOrZero(message = "El stock debe ser mayor o igual que 0")
    @Schema(description = "Stock de la pieza")
    private Integer stock;

    @Builder.Default
    public String image=IMAGE_DEFAULT;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
