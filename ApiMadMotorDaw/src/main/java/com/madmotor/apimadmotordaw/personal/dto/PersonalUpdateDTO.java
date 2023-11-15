package com.madmotor.apimadmotordaw.personal.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data

public class PersonalUpdateDTO {

    @Length(min = 3, max = 150, message = "La dirección debe de tener entre 3 y 150 caracteres")
    private String direccion;

    @Length(min = 20, max = 20, message = "La cuenta de banco debe de contener 20 caracteres, los dos primeros son ES y los demás dígitos")
    private String iban;

}
