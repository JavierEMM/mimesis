package com.mimesis.repository;

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
}
