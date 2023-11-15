package com.madmotor.apimadmotordaw.piezas.repositories;

import com.madmotor.apimadmotordaw.piezas.models.Pieza;

public class PiezasRepository extends JpaRepository<Pieza, Long>, JpaSpecificationExecutor<Pieza>{
}
