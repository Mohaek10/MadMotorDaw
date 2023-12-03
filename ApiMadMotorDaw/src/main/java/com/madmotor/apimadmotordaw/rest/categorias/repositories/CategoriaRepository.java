package com.madmotor.apimadmotordaw.rest.categorias.repositories;


import com.madmotor.apimadmotordaw.rest.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.rest.vehiculos.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * Creamos el repositorio extendéndolo de JPARepository y pasándole el tipo de entidad y el tipo de la clave primaria
 * Con ello ya tenemos las operaciones básicas de CRUD y Paginación gracias JPASpecificationExecutor
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {



    Optional<Categoria> findByNameEqualsIgnoreCase(String nombre);

    Optional<Categoria> findByIdAndIsDeletedFalse(Long id);

    Optional<Categoria> findByNameEqualsIgnoreCaseAndIsDeletedFalse(String nombre);

    List<Categoria> findAllByIsDeletedFalse();

    List<Categoria> findAllByNameContainsIgnoreCase(String nombre);

    List<Categoria> findAllByNameContainsIgnoreCaseAndIsDeletedFalse(String nombre);

    //Actualizar la categoria con isDeleted a true por si tiene vehiculos
    @Modifying
    @Query("UPDATE Categoria p SET p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);

    //Mostrar todos los vehiculos con el id de la categoria
    @Query("SELECT v FROM Vehiculo v WHERE v.categoria.id = :id")
    List<Vehiculo> findAllVehiculosByCategoriaId(Long id);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Vehiculo v WHERE v.categoria.id = :id")
    Boolean existeVehiculoByUd(Long id);

}
