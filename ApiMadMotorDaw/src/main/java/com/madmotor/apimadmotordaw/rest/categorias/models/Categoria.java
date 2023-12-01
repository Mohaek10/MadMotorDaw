package com.madmotor.apimadmotordaw.rest.categorias.models;

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
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Length(min=3, message = "El nombre debe tener al menos 3 caracteres")
    private String name;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    //@Builder.Default
    @CreatedDate //Para que funcione el @CreatedDate hay que añadir @EnableJpaAuditing en la clase principal,
    // Es parte de Spring Data y pueden manejar automáticamente la fecha de creación
    private LocalDateTime createdAt;//=LocalDateTime.now();

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    //@Builder.Default
    @LastModifiedDate //Para que funcione el @LastModifiesDate hay que añadir @EnableJpaAuditing en la clase principal,
    // Es parte de Spring Data y pueden manejar automáticamente la fecha de actualización
    private LocalDateTime updatedAt;// =LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted=false;

}
