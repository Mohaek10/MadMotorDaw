package com.madmotor.apimadmotordaw.rest.auth.services.users;


import com.madmotor.apimadmotordaw.rest.auth.repositories.AuthUsersRepository;
import com.madmotor.apimadmotordaw.rest.users.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;



@Service("userDetailsService")
public class AuthUsersServiceImpl implements AuthUsersService {
    // Indicamos las dependencias del servicio
    private final AuthUsersRepository authUsersRepository;
    // Inyectamos las dependencias
    @Autowired
    public AuthUsersServiceImpl(AuthUsersRepository authUsersRepository) {
        this.authUsersRepository = authUsersRepository;
    }
    /**
     * Método que devuelve un usuario de Spring Security
     * @param username nombre de usuario
     * @return UserDetails com los datos del usuario
     * @throws UserNotFound excepción que se lanza si no se encuentra el usuario 404
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFound {
        return authUsersRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("Usuario con username " + username + " no encontrado"));
    }
}