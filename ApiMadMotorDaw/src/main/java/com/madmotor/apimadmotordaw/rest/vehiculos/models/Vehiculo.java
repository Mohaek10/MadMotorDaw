package com.madmotor.apimadmotordaw.rest.vehiculos.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.CreationTimestamp;
import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "VEHICULOS")
@Schema(name = "Vehiculo", description = "Modelo de vehiculo")
public class Vehiculo {
    public static final String IMAGE_DEFAULT = "https://loremflickr.com/150/150";

    @Schema(description = "Identificador del vehiculo", example = "123e4567-e89b-12d3-a456-426614174000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Schema(description = "Marca del vehiculo", example = "SEAT")
    @Column(name = "marca", nullable = false)
    @NotBlank(message = "Debe de tener una marca")
    private String marca;

    @Schema(description = "Modelo del vehiculo", example = "LEON")
    @Column(name = "modelo", nullable = false)
    @NotBlank(message = "El modelo no puede estar vacio")
    private String modelo;

    @Schema(description = "Año del vehiculo", example = "2021")
    @Column(name = "v_year", nullable = false)
    @Min(value = 1900, message = "El año no puede ser menor a 1900")
    private Integer year;

    @Schema(description = "Kilometros del vehiculo", example = "1000")
    @Column(name = "km", nullable = false)
    @Min(value = 0, message = "Los kilometros no pueden ser negativos")
    private Double km;

    @Schema(description = "Precio del vehiculo", example = "10000")
    @Column(name = "precio", nullable = false)
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Builder.Default
    private Double precio = 0.0;

    @Schema(description = "Stock del vehiculo", example = "10")
    @Column(name = "stock", nullable = false)
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Builder.Default
    private Integer stock = 0;

    @Schema(description = "Imagen del vehiculo", example = IMAGE_DEFAULT)
    @Column(name = "imagen", columnDefinition = "TEXT DEFAULT '" + IMAGE_DEFAULT + "'")
    @Builder.Default
    private String imagen = IMAGE_DEFAULT;

    @Schema(description = "Descripcion del vehiculo", example = "Vehiculo de segunda mano")
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Schema(description = "Fecha de creacion del vehiculo", example = "2021-05-05T12:00:00")
    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Schema(description = "Fecha de actualizacion del vehiculo", example = "2021-05-05T12:00:00")
    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Schema(description = "Categoria del vehiculo", example = "AUtomatico")
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Schema(description = "Si el vehiculo esta borrado", example = "false")
    @Builder.Default
    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;
}