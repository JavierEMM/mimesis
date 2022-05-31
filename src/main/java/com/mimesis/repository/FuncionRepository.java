package com.mimesis.repository;

import com.mimesis.dto.DTOBoletosPorFuncion;
import com.mimesis.dto.DTOTotalBoletosPorFuncion;
import com.mimesis.entity.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface FuncionRepository extends JpaRepository<Funcion,Integer> {
    @Query(value="SELECT f.* FROM funcion f inner join obras o on f.obras_idobras = o.idobras where o.nombre like %?1% ;",nativeQuery = true)
    List<Funcion> listaBuscarFuncionesNombre(String busqueda);

    @Query(value="SELECT f.* FROM funcion f inner join obras o on f.obras_idobras = o.idobras inner join genero  g on o.genero_idgenero = g.idgenero where g.nombre like %?1% ;",nativeQuery = true)
    List<Funcion> listaBuscarFuncionesGenero(String busqueda);

    @Query(nativeQuery = true, value = "SELECT f.* FROM obras o inner join funcion f on f.obras_idobras = o.idobras inner join sala s on s.idsala = f.idsala inner join sede se on se.idsede = s.idsede WHERE se.idsede = ?1 and o.idobras = ?2 and cast(now() AS datetime) < cast(concat(f.fecha,' ',f.horafin) AS datetime)")
    List<Funcion> funcionesPorTeatro(Integer idteatro, Integer idObra);

    @Query(nativeQuery = true, value = "SELECT idfuncion as IdsalaTotal, count(*) as Cantidadboletostotal, sum(estado) as Cantidadasistentes from boleto group by idfuncion;")
    List<DTOTotalBoletosPorFuncion> boletosTotal();

    @Query(nativeQuery = true, value = "SELECT idfuncion as IdFuncionTotal, count(*) as Cantidadboletostotal, sum(estado) as Cantidadasistentes from boleto where idfuncion=?1 ;")
    DTOTotalBoletosPorFuncion boletosbyFuncion(Integer idFuncion);

}
