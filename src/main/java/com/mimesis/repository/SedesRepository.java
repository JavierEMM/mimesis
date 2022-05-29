package com.mimesis.repository;

import com.mimesis.dto.DTOCompararID;
import com.mimesis.entity.Actor;
import com.mimesis.entity.Sala;
import com.mimesis.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SedesRepository extends JpaRepository<Sede,Integer> {
    @Query(nativeQuery = true, value = "SELECT  * FROM sede WHERE nombre like %?1%")
    List<Sede> busquedaTeatro(String nombre);

    @Query(nativeQuery = true, value = "SELECT se.* FROM obras o inner join funcion f on f.obras_idobras = o.idobras inner join sala s on s.idsala = f.idsala inner join sede se on se.idsede = s.idsede WHERE o.idobras = ?1 and cast(now() AS datetime) < cast(concat(f.fecha,' ',f.horafin) AS datetime)")
    List<Sede> teatrosPorFuncion(Integer idobra);


    @Query(value = "select idsala from sala;",nativeQuery = true)
    List<Integer> obtenerIdSala();

    @Query(value = " select idsala from sala where idsede = ?1",nativeQuery = true)
    List<Integer> compararIds(Integer id);

    @Query(value = "SELECT * FROM sede where valido =1",nativeQuery = true)
    List<Sede> sedesvalidas();



}
