package com.mimesis.repository;

import com.mimesis.entity.Actor;
import com.mimesis.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SedesRepository extends JpaRepository<Sede,Integer> {
    @Query(nativeQuery = true, value = "SELECT  * FROM sede WHERE nombre like %?1%")
    List<Sede> busquedaTeatro(String nombre);
}
