package com.madmotor.apimadmotordaw.clientes.Repository;

import com.madmotor.apimadmotordaw.clientes.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ClienteRepository extends JpaRepository<Cliente,String> {


    //Metodo para guardar un cliente pasandolo un objeto de tipo Cliente para el metodo POST y Put en el caso de Spring tambien actualiza
    Cliente save(Cliente cliente);
    //Metodo para encontrar un cliente mediante su dni asi como otra manera de filtrar en el caso de que se necesite
    Optional<Cliente> findByDniEqualsIgnoreCase(String dni);
    //Metodo para recuperar todos los clientes y principalmente para el metodo GET

    List<Cliente> findAll();
}
