package com.mimesis.repository;

import com.mimesis.dto.DTOActoresMejoresCalificados;
import com.mimesis.dto.DTODirectoresMejoresCalificados;
import com.mimesis.entity.Actor;
import com.mimesis.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectorRepository extends JpaRepository<Director,Integer> {
    @Query(nativeQuery = true, value = "SELECT  * FROM director WHERE concat(nombre,' ',apellido) like %?1%")
    List<Director> busquedaDirector(String nombre);

    @Query(nativeQuery = true, value = "SELECT  * FROM director WHERE nombre like %?1%")
    List<Director> busquedaDirectorporNombre(String nombre);

    @Query(value = "select iddirector from calificaciones;",nativeQuery = true)
    List<Integer> obtenerIdCalificacion();

    @Query(nativeQuery = true, value = "SELECT d.nombre as 'nombre_director', o.nombre as 'nombre_obra', c.calificacion as 'calificacion' FROM director d\n" +
            "INNER JOIN funcion f ON d.iddirector=f.iddirector\n" +
            "INNER JOIN obras o ON f.obras_idobras=o.idobras\n" +
            "INNER JOIN calificaciones c ON d.iddirector=c.iddirector;")
    List<DTODirectoresMejoresCalificados> obtenerDirectoresMejoresCalificados();
}
