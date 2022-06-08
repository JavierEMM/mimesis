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

    @Query(nativeQuery = true, value = "SELECT a.nombre as 'nombre_actor', o.nombre as 'nombre_obra', c.calificacion as 'calificacion' FROM actor a\n" +
            "INNER JOIN funciontieneactor fta ON a.idactor = fta.idactor\n" +
            "INNER JOIN funcion f ON fta.idfuncion=f.idfuncion\n" +
            "INNER JOIN obras o ON f.obras_idobras=o.idobras\n" +
            "INNER JOIN calificaciones c ON a.idactor=c.idactor;")
    List<DTOActoresMejoresCalificados> obtenerActoresMejoresCalificados();

}
