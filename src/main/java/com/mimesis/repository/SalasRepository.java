package com.mimesis.repository;

import com.mimesis.dto.DTOFucionPorSala;
import com.mimesis.entity.Funcion;
import com.mimesis.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalasRepository extends JpaRepository<Sala,Integer> {

    @Query(value = "select idsala from funcion;",nativeQuery = true)
    List<Integer> obtenerIdFuncion();

}
