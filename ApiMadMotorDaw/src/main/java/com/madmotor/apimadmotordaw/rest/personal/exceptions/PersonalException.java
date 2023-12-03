package com.madmotor.apimadmotordaw.rest.personal.exceptions;

/**
 * Excepción del personal
 *
 * Excepción personalizada
 * @version 1.0
 * @autor Miguel Vicario
 */

public abstract class PersonalException extends RuntimeException{
     PersonalException(String message){
         super(message);
    }

}
