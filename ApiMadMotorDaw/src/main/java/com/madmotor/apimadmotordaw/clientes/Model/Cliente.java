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
@Table(name = "Cliente")
public class Cliente {
    public static final String IMAGE_DEFAULT = "https://via.placeholder.com/150";
    //Campo inmutable para que el cliente se unico
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID codCliente;
    @NonNull
    @NotBlank(message = "Necesitamos conocer su nombre")
    private String nombre;
    @NonNull
    @NotBlank(message="Necesitamos conocer sus apellidos")
    private String apellido;
    private String direccion;
    private Integer codigoPostal;
    @NonNull
    @NotBlank(message = "Es Obligatorio el DNI")
    private String dni;
    private Boolean piezas;
    private Boolean coches;
    @Column(columnDefinition = "TEXT default '" + IMAGE_DEFAULT + "'") // Por defecto una imagen
    @Schema(description = "Foto de perfil del cliente")
    @Builder.Default
    private String imagen = IMAGE_DEFAULT;
}
