package com.madmotor.apimadmotordaw.categorias.repositories;


import com.madmotor.apimadmotordaw.categorias.models.Categoria;
import com.madmotor.apimadmotordaw.vehiculos.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNameEqualsIgnoreCase(String nombre);

    Optional<Categoria> findByIdAndIsDeletedFalse(Long id);

    Optional<Categoria> findByNameEqualsIgnoreCaseAndIsDeletedFalse(String nombre);

    List<Categoria> findAllByIsDeletedFalse();

    List<Categoria> findAllByNameContainsIgnoreCase(String nombre);

    List<Categoria> findAllByNameContainsIgnoreCaseAndIsDeletedFalse(String nombre);

    @Modifying
    @Query("UPDATE Categoria p SET p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(Long id);

    //Mostrar todos los vehiculos con el id de la categoria
    @Query("SELECT v FROM Vehiculo v WHERE v.categoria.id = :id")
    List<Vehiculo> findAllVehiculosByCategoriaId(Long id);

}
