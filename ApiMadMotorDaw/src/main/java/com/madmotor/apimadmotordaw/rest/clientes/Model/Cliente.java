package com.madmotor.apimadmotordaw.rest.clientes.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CLIENTES")
@Schema(description = "Cliente ")
public class Cliente {
    public static final String IMAGE_DEFAULT = "https://via.placeholder.com/150";
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador del cliente")
    private UUID id;
    @Schema(description = "Nombre del cliente")
    @NotNull
    @NotBlank(message = "Necesitamos conocer su nombre")
    @Column(name="nombre" ,nullable = false)
    private String nombre;
    @Schema(description = "Apellido del cliente")
    @NotNull
    @NotBlank(message="Necesitamos conocer sus apellidos")
    @Column(name="apellido" ,nullable = false)
    private String apellido;
    @Schema(description = "Direccion del cliente")
    @NotNull
    @Column(name ="direccion" ,nullable = false)
    private String direccion;
    @Schema(description = "Codigo postal del cliente")
    @NotNull
    @Column(name="codigo_Postal" ,nullable = false)
    private Integer codigoPostal;
    @Schema(description = "Dni del cliente")
    @NotNull
    @NotBlank(message = "Es Obligatorio el DNI")
    @Column(name="dni" ,nullable = false)
    private String dni;
    @Column(columnDefinition = "TEXT default '" + IMAGE_DEFAULT + "'") // Por defecto una imagen
    @Schema(description = "Foto de perfil del cliente")
    @Builder.Default
    private String imagen = IMAGE_DEFAULT;
    @Schema(description = "Valor booleano del cliente")
    @Builder.Default
    @Column( columnDefinition = "boolean default false")
    private Boolean isDeleted=false;
    @Schema(description = "Fecha de creacion del cliente")
    @CreationTimestamp
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Schema(description = "Fecha de actualizacion del cliente")
    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}