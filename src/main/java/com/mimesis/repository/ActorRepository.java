package com.mimesis.repository;

import com.mimesis.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor,Integer> {
    @Query(nativeQuery = true, value = "SELECT  * FROM actor WHERE concat(nombre,' ',apellido) like %?1%")
    List<Actor> busquedaActor(String nombre);
}
