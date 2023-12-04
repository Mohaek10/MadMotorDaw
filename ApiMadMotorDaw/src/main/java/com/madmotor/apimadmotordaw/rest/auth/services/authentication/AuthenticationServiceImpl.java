package com.madmotor.apimadmotordaw.rest.auth.services.authentication;


import com.madmotor.apimadmotordaw.rest.auth.dto.JwtAuthResponse;
import com.madmotor.apimadmotordaw.rest.auth.dto.UserSignInRequest;
import com.madmotor.apimadmotordaw.rest.auth.dto.UserSignUpRequest;
import com.madmotor.apimadmotordaw.rest.auth.exceptions.AuthSingInInvalid;
import com.madmotor.apimadmotordaw.rest.auth.exceptions.UserAuthNameOrEmailExisten;
import com.madmotor.apimadmotordaw.rest.auth.exceptions.UserDiferentePasswords;
import com.madmotor.apimadmotordaw.rest.auth.repositories.AuthUsersRepository;
import com.madmotor.apimadmotordaw.rest.auth.services.jwt.JwtService;
import com.madmotor.apimadmotordaw.rest.users.models.Role;
import com.madmotor.apimadmotordaw.rest.users.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    // Indicamos que las dependencias que necesitamos para el framework
    private final AuthUsersRepository authUsersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    // Inyectamos las dependencias
    @Autowired
    public AuthenticationServiceImpl(AuthUsersRepository authUsersRepository, PasswordEncoder passwordEncoder,
                                     JwtService jwtService, AuthenticationManager authenticationManager) {
        this.authUsersRepository = authUsersRepository;
        // Codificador de contraseñas
        this.passwordEncoder = passwordEncoder;
        // Servicio de JWT
        this.jwtService = jwtService;
        // Gestor de autenticación
        this.authenticationManager = authenticationManager;
    }


    /**
     * Registra un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     * throws UserAuthNameOrEmailExisten si el nombre de usuario o el email ya existen 404
     * throws UserDiferentePasswords si las contraseñas no coinciden 400
     */

    @Override
    public JwtAuthResponse signUp(UserSignUpRequest request) {
        log.info("Creando usuario: {}", request);
        if (request.getPassword().contentEquals(request.getPasswordComprobacion())) {
            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .nombre(request.getNombre())
                    .apellidos(request.getApellidos())
                    .roles(Stream.of(Role.USER).collect(Collectors.toSet()))
                    .build();
            try {
                var userStored = authUsersRepository.save(user);
                return JwtAuthResponse.builder().token(jwtService.generateToken(userStored)).build();
            } catch (DataIntegrityViolationException ex) {
                throw new UserAuthNameOrEmailExisten("El usuario con username " + request.getUsername() + " o email " + request.getEmail() + " ya existe");
            }
        } else {
            throw new UserDiferentePasswords("Las contraseñas no coinciden");

        }
    }


    /**
     * Autentica un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     * throws AuthSingInInvalid si el usuario o la contraseña son incorrectos 404
     */

    @Override
    public JwtAuthResponse signIn(UserSignInRequest request) {
        log.info("Autenticando usuario: {}", request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = authUsersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthSingInInvalid("Usuario o contraseña incorrectos"));
        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).build();
    }
}