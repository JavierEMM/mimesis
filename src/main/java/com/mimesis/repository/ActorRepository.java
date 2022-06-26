package com.mimesis.repository;

import com.mimesis.dto.DTOActoresMejoresCalificados;
import com.mimesis.entity.Actor;
import com.mimesis.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor,Integer> {
    @Query(nativeQuery = true, value = "SELECT  * FROM actor WHERE concat(nombre,' ',apellido) like %?1%;")
    List<Actor> busquedaActor(String nombre);

    @Query(nativeQuery = true, value = "SELECT  * FROM actor WHERE nombre like %?1%")
    List<Actor> busquedaActorporNombre(String nombre);

    @Query(value = "select idactor from calificaciones where idactor is not null;",nativeQuery = true)
    List<Integer> obtenerIdCalificacion();

    @Query(nativeQuery = true, value = "SELECT a.nombre, a.apellido, avg(c.calificacion) as calificacion  FROM mimesis.calificaciones  c \n" +
            "inner join actor a on a.idactor=c.idactor \n" +
            "where c.idactor IS NOT NULL group by c.idactor;")
    List<DTOActoresMejoresCalificados> obtenerActoresMejoresCalificados();

    @Query(nativeQuery = true, value = "SELECT * FROM actor a WHERE a.nombre or a.apellido like %?1%;")
    List<Actor> busquedaBestoAct(String busqueda);

}
