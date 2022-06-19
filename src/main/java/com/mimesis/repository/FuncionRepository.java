package com.mimesis.repository;

import com.mimesis.dto.DTOBoletosPorFuncion;
import com.mimesis.dto.DTOBoletosValidos;
import com.mimesis.dto.DTOTotalBoletosPorFuncion;
import com.mimesis.entity.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionRepository extends JpaRepository<Funcion,Integer> {
    @Query(value="SELECT f.* FROM funcion f inner join obras o on f.obras_idobras = o.idobras where o.nombre like %?1% ;",nativeQuery = true)
    List<Funcion> listaBuscarFuncionesNombre(String busqueda);

    @Query(value="SELECT f.* FROM funcion f inner join obras o on f.obras_idobras = o.idobras inner join genero  g on o.genero_idgenero = g.idgenero where g.nombre like %?1% ;",nativeQuery = true)
    List<Funcion> listaBuscarFuncionesGenero(String busqueda);

    @Query(nativeQuery = true, value = "SELECT f.* FROM obras o inner join funcion f on f.obras_idobras = o.idobras inner join sala s on s.idsala = f.idsala inner join sede se on se.idsede = s.idsede WHERE se.idsede = ?1 and o.idobras = ?2 and cast(now() AS datetime) < cast(concat(f.fecha,' ',f.horafin) AS datetime)")
    List<Funcion> funcionesPorTeatro(Integer idteatro, Integer idObra);

    @Query(nativeQuery = true, value = "SELECT idfuncion as IdFuncionTotal, count(*) as Cantidadboletostotal, sum(estado) as Cantidadasistentes from boleto group by idfuncion;")
    List<DTOTotalBoletosPorFuncion> boletosTotal();

    @Query(nativeQuery = true, value = " SELECT b.idfuncion as IdFuncionTotal, count(*) as Cantidadboletostotal, sum(b.estado) as Cantidadasistentes from boleto b inner join funcion f on f.idfuncion = b.idfuncion where f.fecha>= ?1 and f.fecha<= ?2 group by f.idfuncion;")
    List<DTOTotalBoletosPorFuncion> boletosTotalFecha(String fechaInicio, String fechaFin);

    @Query(nativeQuery = true, value = "SELECT idfuncion as IdFuncionTotal, count(*) as Cantidadboletostotal, sum(estado) as Cantidadasistentes from boleto where idfuncion=?1 ;")
    DTOTotalBoletosPorFuncion boletosbyFuncion(Integer idFuncion);

    @Query(value="SELECT * FROM mimesis.funcion where fecha >= ?1 and fecha <= ?2 order by fecha asc;",nativeQuery = true)
    List<Funcion> listaBuscarFuncionesFecha(String fInicio,String fFin);

    @Query(nativeQuery = true,value = "SELECT f.idfuncion,f.fecha,f.aforo,f.horainicio,f.iddirector,f.horafin,f.costo,f.idsala,f.valido,f.obras_idobras as 'idobras',(f.aforo-count(b.estado)) as 'boletos' " +
            "FROM mimesis.boleto b INNER JOIN funcion f ON f.idfuncion = b.idfuncion INNER JOIN sala sa ON sa.idsala = f.idsala INNER JOIN sede se ON se.idsede = sa.idsede " +
            "WHERE se.idsede = ?1 and f.obras_idobras = ?2 and cast(now() AS datetime) < cast(concat(f.fecha,' ',f.horafin) AS datetime) group by b.idfuncion")
    List<DTOBoletosValidos> listaFuncionesValidos(Integer idSede, Integer idObra);

    @Query(nativeQuery = true,value = "SELECT f.idfuncion,f.fecha,f.aforo,f.horainicio,f.iddirector,f.horafin,f.costo,f.idsala,f.valido,f.obras_idobras \n" +
            "            FROM funcion f  INNER JOIN sala sa ON sa.idsala = f.idsala INNER JOIN sede se ON se.idsede = sa.idsede \n" +
            "            WHERE se.idsede = ?1 and f.obras_idobras = ?2 and cast(now() AS datetime) < cast(concat(f.fecha,' ',f.horafin) AS datetime) group by f.idfuncion;")
    List<Funcion> listaFuncionesValidosCorregida(Integer idSede, Integer idObra);


    @Query(nativeQuery = true,value = "SELECT f.aforo - count(estado) as boletos FROM mimesis.boleto b inner join funcion f on b.idfuncion = f.idfuncion where b.idfuncion=?1 group by b.idfuncion;")
    Optional<Integer> obtenerBoletos(Integer idfuncion);
}
