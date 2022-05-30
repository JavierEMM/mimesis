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
    @Query(nativeQuery = true, value = "SELECT idfuncion as idsalaBoleto, count(*) as SumaBoletos from boleto group by idfuncion;")
    List<DTOBoletosPorFuncion> boletosporFuncion();
    @Query(nativeQuery = true, value = "SELECT costo from funcion order by idfuncion;")
    List<Double> costoPorFuncion();

}
