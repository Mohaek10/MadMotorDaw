package com.madmotor.apimadmotordaw.rest.auth.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * JwtService
 *  Uso de la libreria jjwt para generar y validar tokens
 *  UserDetails es una interfaz de Spring Security que guarda la inforamción del usuario
 */

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}