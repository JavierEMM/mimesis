package com.mimesis.repository;

import com.mimesis.entity.Calificacion;
import com.mimesis.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalificacionRepository extends JpaRepository<Calificacion,Integer> {
}
