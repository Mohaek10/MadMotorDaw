package com.madmotor.apimadmotordaw.Piezas.repositories;

import com.madmotor.apimadmotordaw.Piezas.models.Pieza;

public class PiezasRepository extends JpaRepository<Pieza, Long>, JpaSpecificationExecutor<Pieza>{
}
