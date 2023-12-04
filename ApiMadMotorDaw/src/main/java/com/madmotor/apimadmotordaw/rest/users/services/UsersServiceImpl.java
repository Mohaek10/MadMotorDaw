package com.madmotor.apimadmotordaw.rest.users.services;


import com.madmotor.apimadmotordaw.rest.pedidos.repositories.PedidoRepository;
import com.madmotor.apimadmotordaw.rest.users.dto.UserInfoResponse;
import com.madmotor.apimadmotordaw.rest.users.dto.UserRequest;
import com.madmotor.apimadmotordaw.rest.users.dto.UserResponse;
import com.madmotor.apimadmotordaw.rest.users.exceptions.UserNameOrEmailExists;
import com.madmotor.apimadmotordaw.rest.users.exceptions.UserNotFound;
import com.madmotor.apimadmotordaw.rest.users.mappers.UsersMapper;
import com.madmotor.apimadmotordaw.rest.users.models.User;
import com.madmotor.apimadmotordaw.rest.users.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
/**
 *  Implementación del servicio de la entidad User
 *  - @Service: Indica que es un servicio de la capa de negocio
 *  - @Slf4j: Para la gestión de logs
 *  - @CacheConfig: Para la configuración de la caché
 */

@Service
@Slf4j
@CacheConfig(cacheNames = {"users"})
public class UsersServiceImpl implements UsersService {
    // Indicamos las dependencias que vamos a usar

    private final UsersRepository usersRepository;
    private final PedidoRepository pedidosRepository;
    private final UsersMapper usersMapper;
// Inyectamos las dependencias por constructor
    public UsersServiceImpl(UsersRepository usersRepository, PedidoRepository pedidosRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.pedidosRepository = pedidosRepository;
        this.usersMapper = usersMapper;
    }
    /**
     * Busca todos los usuarios con los criterios de búsqueda
     * @param username (opcional) nombre de usuario
     * @param email (opcional) email correo electrónico
     * @param isDeleted (opcional) borrado
     * @return Page<UserResponse> Lista de usuarios paginada
     */

    @Override
    public Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable) {
        // Especificación de los criterios de búsqueda
        // Si no se ha introducido ningún criterio de búsqueda se devuelven todos los usuarios
        // Si se ha introducido algún criterio de búsqueda se devuelven los clientes que coincidan con los criterios de búsqueda
        // Se devuelven los usuarios que coincidan con los criterios de búsqueda
        log.info("Buscando todos los usuarios con username: " + username + " y borrados: " + isDeleted);

        //Criterio de búsqueda por username

        Specification<User> specUsernameUser = (root, query, criteriaBuilder) ->
                username.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<User> specEmailUser = (root, query, criteriaBuilder) ->
                email.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<User> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(m -> criteriaBuilder.equal(root.get("isDeleted"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<User> criterio = Specification.where(specUsernameUser)
                .and(specEmailUser)
                .and(specIsDeleted);

        return usersRepository.findAll(criterio, pageable).map(usersMapper::toUserResponse);
    }

    /**
     * Busca un usuario por id
     * @param id id del usuario
     * @return UserInfoResponse Usuario con sus pedidos
     * @throws UserNotFound si no existe el usuario
     */
    @Override
    @Cacheable(key = "#id")
    public UserInfoResponse findById(String id) {
        log.info("Buscando usuario por id: " + id);
        var user = usersRepository.findById(UUID.fromString(id)).orElseThrow(() -> new UserNotFound(id.toString()));
        var pedidos = pedidosRepository.findPedidosIdsByIdUsuario(UUID.fromString(id)).stream().map(p -> p.getId().toHexString()).toList();
        return usersMapper.toUserInfoResponse(user, pedidos);
    }
    /**
     * Guarda un usuario
     * @param userRequest datos del usuario
     * @return UserResponse usuario guardado
     * @throws UserNameOrEmailExists si ya existe un usuario con el mismo username o email
     */
    @Override
    @CachePut(key = "#result.id")
    public UserResponse save(UserRequest userRequest) {
        log.info("Guardando usuario: " + userRequest);
        usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(u -> {
                    throw new UserNameOrEmailExists("Ya existe un usuario con ese username o email");
                });
        return usersMapper.toUserResponse(usersRepository.save(usersMapper.toUser(userRequest)));
    }
    /**
     * Actualiza un usuario
     * @param id id del usuario
     * @param userRequest datos del usuario
     * @return UserResponse usuario actualizado
     * @throws UserNotFound si no existe el usuario
     * @throws UserNameOrEmailExists si ya existe un usuario con el mismo username o email soy yo mismo
     */
    @Override
    @CachePut(key = "#result.id")
    public UserResponse update(String id, UserRequest userRequest) {
        log.info("Actualizando usuario: " + userRequest);
        usersRepository.findById(UUID.fromString(id)).orElseThrow(() -> new UserNotFound(id));
        usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        System.out.println("usuario encontrado: " + u.getId() + " Mi id: " + id);
                        throw new UserNameOrEmailExists("Ya existe un usuario con ese username o email");
                    }
                });
        return usersMapper.toUserResponse(usersRepository.save(usersMapper.toUser(userRequest, UUID.fromString(id))));
    }
    /**
     * Borra un usuario por id
     * @param id id del usuario
     * @throws UserNotFound si no existe el usuario
     */

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(String id) {
        log.info("Borrando usuario por id: " + id);
        User user = usersRepository.findById(UUID.fromString(id)).orElseThrow(() -> new UserNotFound(id.toString()));
        if (pedidosRepository.existsByIdUsuario(UUID.fromString(id))) {
            log.info("Borrado lógico de usuario por id: " + id);
            usersRepository.updateIsDeletedToTrueById(UUID.fromString(id));
        } else {
            log.info("Borrado físico de usuario por id: " + id);
            usersRepository.delete(user);
        }
    }
}
