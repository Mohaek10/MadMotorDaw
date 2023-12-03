package com.madmotor.apimadmotordaw.rest.categorias.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "CATEGORIAS")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "Categoría de un producto modelo")
public class Categoria {
    @Schema(description = "Identificador único de la categoría", example = "1L", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Nombre de la categoría", example = "Automatico", required = true)
    @Column(name = "name", nullable = false)
    @Length(min=3, message = "El nombre debe tener al menos 3 caracteres")
    private String name;
    @Schema(description = "Momento de creacion de la categoria", example = "2023-1-01T00:00:00.000000")
    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    //@Builder.Default
    @CreatedDate //Para que funcione el @CreatedDate hay que añadir @EnableJpaAuditing en la clase principal,
    // Es parte de Spring Data y pueden manejar automáticamente la fecha de creación
    private LocalDateTime createdAt;//=LocalDateTime.now();
    @Schema(description = "Momento de actualizacion de la categoria", example = "2023-1-01T01:00:00.000000")
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    //@Builder.Default
    @LastModifiedDate //Para que funcione el @LastModifiesDate hay que añadir @EnableJpaAuditing en la clase principal,
    // Es parte de Spring Data y pueden manejar automáticamente la fecha de actualización
    private LocalDateTime updatedAt;// =LocalDateTime.now();
    @Schema(description = "Si la categoria ha sido borrada", example = "false")
    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted=false;

}
