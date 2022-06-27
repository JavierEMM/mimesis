package com.mimesis.repository;

import com.mimesis.entity.Calificacion;
import com.mimesis.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion,Integer> {

    @Query(nativeQuery = true,value = "SELECT * FROM calificaciones WHERE idusuario = ?1")
    List<Calificacion> findUsuario(Integer id);
}
