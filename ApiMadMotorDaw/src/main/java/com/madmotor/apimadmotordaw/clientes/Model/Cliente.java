package com.madmotor.apimadmotordaw.clientes.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CLIENTES")
public class Cliente {
    public static final String IMAGE_DEFAULT = "https://via.placeholder.com/150";
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NonNull
    @NotBlank(message = "Necesitamos conocer su nombre")
    @Column(name="nombre" ,nullable = false)
    private String nombre;
    @NonNull
    @NotBlank(message="Necesitamos conocer sus apellidos")
    @Column(name="apellido" ,nullable = false)
    private String apellido;
    @NonNull
    @Column(name ="direccion" ,nullable = false)
    private String direccion;
    @NonNull
    @Column(name="codigo_Postal" ,nullable = false)
    private Integer codigoPostal;
    @NonNull
    @NotBlank(message = "Es Obligatorio el DNI")
    @Column(name="dni" ,nullable = false)
    private String dni;
    @NonNull
    @Column(name="piezas" ,nullable = false)
    private Boolean piezas;
    @Column(name="coches" ,nullable = false)
    private Boolean coches;
    @Column(columnDefinition = "TEXT default '" + IMAGE_DEFAULT + "'") // Por defecto una imagen
    @Schema(description = "Foto de perfil del cliente")
    @Builder.Default
    private String imagen = IMAGE_DEFAULT;
}
