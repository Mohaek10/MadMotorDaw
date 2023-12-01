package com.madmotor.apimadmotordaw.rest.vehiculos.models;

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
public class Vehiculo {
    public static final String IMAGE_DEFAULT = "https://loremflickr.com/150/150";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id = UUID.randomUUID();

    @Column(name = "marca", nullable = false)
    @NotBlank(message = "Debe de tener una marca")
    private String marca;

    @Column(name = "modelo", nullable = false)
    @NotBlank(message = "El modelo no puede estar vacio")
    private String modelo;

    @Column(name = "v_year", nullable = false)
    @Min(value = 1900, message = "El año no puede ser menor a 1900")
    private Integer year;

    @Column(name = "km", nullable = false)
    @Min(value = 0, message = "Los kilometros no pueden ser negativos")
    private Double km;

    @Column(name = "precio", nullable = false)
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Builder.Default
    private Double precio = 0.0;

    @Column(name = "stock", nullable = false)
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Builder.Default
    private Integer stock = 0;

    @Column(name = "imagen", columnDefinition = "TEXT DEFAULT '" + IMAGE_DEFAULT + "'")
    @Builder.Default
    private String imagen = IMAGE_DEFAULT;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();


    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Builder.Default
    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;
}