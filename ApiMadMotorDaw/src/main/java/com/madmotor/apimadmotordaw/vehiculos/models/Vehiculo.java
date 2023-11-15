package com.madmotor.apimadmotordaw.vehiculos.models;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.CreationTimestamp;
import com.madmotor.apimadmotordaw.categorias.models.Categoria;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "VEHICULOS")
@EntityListeners(AuditingEntityListener.class)
public class Vehiculo {
    public static final String IMAGE_DEFAULT = "https://loremflickr.com/150/150";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id=UUID.randomUUID();

    @NotBlank(message = "Debe de tener una marca")
    private String marca;

    @NotBlank(message = "El modelo no puede estar vacio")
    private String modelo;

    @Min(value = 0, message = "El precio no puede ser negativo")
    @Builder.Default
    private Double precio=0.0;

    @Min(value =0, message = "El stock no puede ser negativo")
    @Builder.Default
    private Integer stock=0;

    @Column (columnDefinition="TEXT DEFAULT '"+IMAGE_DEFAULT+"'")
    @Builder.Default
    private String imagen=IMAGE_DEFAULT;

    @NotBlank(message = "La descripcion no puede estar vacia")
    private String descripcion;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt=LocalDateTime.now();

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt=LocalDateTime.now();

    @Builder.Default
    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted=false;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


}