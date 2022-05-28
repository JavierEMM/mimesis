package com.mimesis.repository;

import com.mimesis.entity.Funcion;
import com.mimesis.entity.Obra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObrasRepository extends JpaRepository<Obra,Integer> {
    @Query(value="SELECT o.* FROM funcion f inner join obras o on f.obras_idobras = o.idobras where o.nombre like %?1% ;",nativeQuery = true)
    List<Obra> listaBuscarObrasNombre(String busqueda);

    @Query(value="SELECT o.* FROM obras o inner join genero  g on o.genero_idgenero = g.idgenero where o.nombre like %?1% limit 6",nativeQuery = true)
    List<Obra> listaBuscarObrasUnicasPorNombre(String busqueda);

    @Query(value="SELECT o.* FROM funcion f inner join obras o on f.obras_idobras = o.idobras inner join genero  g on o.genero_idgenero = g.idgenero where g.nombre like %?1% ;",nativeQuery = true)
    List<Obra> listaBuscarObrasGenero(String busqueda);

    Obra findByNombre(String nombre);
}
