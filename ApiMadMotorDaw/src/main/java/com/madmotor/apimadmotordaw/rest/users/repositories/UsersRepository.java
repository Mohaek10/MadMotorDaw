package com.madmotor.apimadmotordaw.rest.users.repositories;

import com.madmotor.apimadmotordaw.rest.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Reposito de la entidad User
 *  - JpaRepository: Proporciona el CRUD
 *  - JpaSpecificationExecutor: Proporciona el filtrado
 */
@Repository
public interface UsersRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameEqualsIgnoreCase(String username);

    Optional<User> findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(String username, String email);

    // Categorias por nombre
    List<User> findAllByUsernameContainingIgnoreCase(String username);

    @Modifying
    @Query("UPDATE User p SET p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(UUID id);

}