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
    @Query(nativeQuery = true, value = "SELECT  * FROM actor WHERE concat(nombre,' ',apellido) like %?1%")
    List<Actor> busquedaActor(String nombre);

    @Query(nativeQuery = true, value = "SELECT  * FROM actor WHERE nombre like %?1%")
    List<Actor> busquedaActorporNombre(String nombre);

    @Query(value = "select idactor from calificaciones where idactor is not null;",nativeQuery = true)
    List<Integer> obtenerIdCalificacion();

    @Query(nativeQuery = true, value = "SELECT CONCAT(a.nombre, a.apellido) as nombre, o.nombre as obra, AVG(calificacion) as calificacion FROM calificaciones c\n" +
            "INNER JOIN actor a ON c.idactor=a.idactor\n" +
            "INNER JOIN funcion f ON f.idfuncion=c.idfuncion\n" +
            "INNER JOIN obras o ON o.idobras=f.obras_idobras\n" +
            "WHERE c.idactor IS NOT NULL")
    List<DTOActoresMejoresCalificados> obtenerActoresMejoresCalificados();

}
