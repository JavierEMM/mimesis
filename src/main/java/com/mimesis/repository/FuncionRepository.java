package com.mimesis.repository;

import com.mimesis.entity.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface FuncionRepository extends JpaRepository<Funcion,Integer> {
    @Query(value="SELECT  * FROM funcion where nombre like %?1%",nativeQuery = true)
    List<Funcion> listaBuscarFuncionesNombre(String busqueda);

    @Query(value="SELECT  * FROM funcion where genero like %?1%",nativeQuery = true)
    List<Funcion> listaBuscarFuncionesGenero(String busqueda);
}
