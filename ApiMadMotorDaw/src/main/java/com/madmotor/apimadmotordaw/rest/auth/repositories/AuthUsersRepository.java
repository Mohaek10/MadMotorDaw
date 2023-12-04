package com.madmotor.apimadmotordaw.rest.auth.repositories;

import com.madmotor.apimadmotordaw.rest.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repositorio de usuarios para autenticación
 * Uso de Spring Data JPA
 */

@Repository
public interface AuthUsersRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}