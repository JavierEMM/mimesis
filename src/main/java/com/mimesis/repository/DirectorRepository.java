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

    @Query(value = "select iddirector from calificaciones where iddirector is not null;",nativeQuery = true)
    List<Integer> obtenerIdCalificacion();

    @Query(nativeQuery = true, value = "SELECT d.nombre, d.apellido, avg(c.calificacion) as 'calificacion' FROM director d\n" +
            "INNER JOIN calificaciones c ON d.iddirector=c.iddirector\n" +
            "where d.iddirector IS NOT NULL group by d.iddirector;")
    List<DTODirectoresMejoresCalificados> obtenerDirectoresMejoresCalificados();

    @Query(nativeQuery = true, value = "SELECT d.nombre, d.apellido, avg(c.calificacion) as calificacion FROM director d\n" +
            "inner join calificaciones c on d.iddirector=c.iddirector\n" +
            "WHERE concat(d.nombre,' ',d.apellido) like %?1% group by d.iddirector")
    List<DTODirectoresMejoresCalificados> busquedaBestoDir(String busqueda);
}
