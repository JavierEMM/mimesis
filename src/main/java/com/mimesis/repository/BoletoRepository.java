package com.mimesis.repository;

import com.mimesis.dto.DTOBoletosPorFuncion;
import com.mimesis.entity.Actor;
import com.mimesis.entity.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoletoRepository extends JpaRepository<Boleto,Integer> {
    @Query(nativeQuery = true, value = "SELECT idfuncion as idsalaBoleto, count(estado) as SumaBoletos from boleto group by idfuncion;")
    List<DTOBoletosPorFuncion> boletosporFuncion();
    @Query(nativeQuery = true, value = "SELECT boleto.idfuncion as idsalaBoleto, count(estado) as SumaBoletos from boleto inner join funcion on boleto.idfuncion = funcion.idfuncion and funcion.fecha >= ?1 and funcion.fecha <= ?2 group by boleto.idfuncion order by SumaBoletos desc;")
    List<DTOBoletosPorFuncion> boletosporFuncionFechasMas(String fechaInicio, String fechaFin);
    @Query(nativeQuery = true, value = "SELECT boleto.idfuncion as idsalaBoleto, count(estado) as SumaBoletos from boleto inner join funcion on boleto.idfuncion = funcion.idfuncion and funcion.fecha >= ?1 and funcion.fecha <= ?2 group by boleto.idfuncion order by SumaBoletos asc;")
    List<DTOBoletosPorFuncion> boletosporFuncionFechasMenos(String fechaInicio, String fechaFin);
    @Query(nativeQuery = true, value = "SELECT costo from funcion order by idfuncion;")
    List<Double> costoPorFuncion();
    @Query(nativeQuery = true, value = "select b.idboleto from funcion f \n" +
            "inner join boleto b on f.idfuncion = b.idfuncion\n" +
            "inner join usuario u on b.idusuario = u.idusuario where u.idusuario=?1 and f.idfuncion=?2")
    List<Integer> boletosPorUsuarioFuncion(Integer idusuario, Integer idfuncion);
}
