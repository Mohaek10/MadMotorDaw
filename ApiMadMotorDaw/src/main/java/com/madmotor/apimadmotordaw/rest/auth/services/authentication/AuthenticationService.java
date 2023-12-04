package com.madmotor.apimadmotordaw.rest.auth.services.authentication;


import com.madmotor.apimadmotordaw.rest.auth.dto.JwtAuthResponse;
import com.madmotor.apimadmotordaw.rest.auth.dto.UserSignInRequest;
import com.madmotor.apimadmotordaw.rest.auth.dto.UserSignUpRequest;
/**
 * Interface que define los métodos de autenticación
 */

public interface AuthenticationService {
    JwtAuthResponse signUp(UserSignUpRequest request);

    JwtAuthResponse signIn(UserSignInRequest request);
}