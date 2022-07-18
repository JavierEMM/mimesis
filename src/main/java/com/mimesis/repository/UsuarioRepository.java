package com.mimesis.repository;

import com.mimesis.dto.*;
import com.mimesis.entity.Boleto;
import com.mimesis.entity.Obra;
import com.mimesis.entity.Sede;
import com.mimesis.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Usuario findByToken(String token);

    Usuario findByResetpwdtoken(String resetpwdtoken);

    List<Usuario> findByRol(String rol);

    Usuario findByCorreo(String correo);

    @Query(value="select u.nombre as nombre, u.apellido as apellido, u.dni as dni, u.correo as correo, u.direccion as direccion, se.nombre as nombresede\n" +
            "from usuario u\n" +
            "inner join boleto b on (u.idusuario = b.idusuario) \n" +
            "inner join funcion f on (b.idfuncion = f.idfuncion)\n" +
            "inner join sala sa on (sa.idsala = f.idsala)\n" +
            "inner join sede se on (se.idsede = sa.idsede)\n" +
            "where u.rol = \"Cliente\" and se.idsede = 11;", nativeQuery = true)
    List<ClientesporSedeDTO> obtenerClientesporSede();

    @Query(value="INSERT INTO usuario (nombre, apellido, correo, contrasena, rol, valido, emailconfirm)\n" +
            "VALUES (?1,?2,?3,?4,?5,1,1);",nativeQuery = true)
    void agregarOperadores(String nombre,String apellido, String correo, String contrasena, String rol);


    @Query(nativeQuery = true, value = "Select b.idfuncion as idfuncion,count(a.idboleto) as cantidad,b.costo*count(a.idboleto) as costototal, MAX(a.estado) as estado " +
            "from boleto a left join funcion b on a.idfuncion=b.idfuncion\n" +
            "            left join obras c on b.obras_idobras = c.idobras\n" +
            "            left join sala d on b.idsala = d.idsala\n" +
            "            left join sede e on d.idsede = e.idsede\n" +
            "            where a.idusuario =?1 group by b.idfuncion,b.horainicio,c.idobras,c.nombre,e.nombre;")
    List<DTOHistorial> ObtenerHistorial(Integer id);


    @Query(nativeQuery = true, value = "Select f.foto as fotoobra, o.nombre as nombreobra, p.calificacion as calificacionobra,f.idobras as idobra\n" +
            "from fotos f left join funcion c on f.idobras = c.obras_idobras\n" +
            "left join obras o on c.obras_idobras = o.idobras\n" +
            "left join calificaciones p on c.idfuncion = p.idfuncion\n" +
            "left join boleto b on b.idfuncion = c.idfuncion\n" +
            "where p.idusuario=?1 and p.idfuncion=?2 group by p.calificacion;")
    List<DTOCalificacionObra> ObtenerCalificacionObra(Integer id, Integer idfuncion);

    @Query(nativeQuery = true, value = "Select g.foto as fotodirector,g.nombre as nombredirector,g.apellido as apellidodirector,g.correo as correodirector,p.calificacion as calificaciondirector, g.iddirector as fotodirectores\n" +
            "from director g left join calificaciones p on g.iddirector = p.iddirector\n" +
            "left join funcion c on g.iddirector = c.iddirector\n" +
            "where p.idusuario = ?1 and p.idfuncion= ?2 group by g.foto,g.nombre,g.apellido,g.correo,p.calificacion;")
    List<DTOCalificacionDirector> ObtenerCalificacionDirector(Integer idusuario, Integer idfuncion);

    @Query(nativeQuery = true, value = "Select b.idfuncion as idfuncion,count(a.idboleto) as cantidad,b.costo*count(a.idboleto) as costototal, MAX(a.estado) as estado " +
            "from boleto a left join funcion b on a.idfuncion=b.idfuncion\n" +
            "            left join obras c on b.obras_idobras = c.idobras\n" +
            "            left join sala d on b.idsala = d.idsala\n" +
            "            left join sede e on d.idsede = e.idsede\n" +
            "            where a.idusuario =?1 and c.nombre like %?2% group by b.idfuncion,b.horainicio,c.idobras,c.nombre,e.nombre;")
    List<DTOHistorial> ObtenerHistorialporObra(Integer id,String nombre);

    @Query(nativeQuery = true, value = "Select b.idfuncion as idfuncion,count(a.idboleto) as cantidad,b.costo*count(a.idboleto) as costototal, MAX(a.estado) as estado " +
            "            from boleto a left join funcion b on a.idfuncion=b.idfuncion\n" +
            "                       left join obras c on b.obras_idobras = c.idobras\n" +
            "                       left join sala d on b.idsala = d.idsala\n" +
            "                        left join sede e on d.idsede = e.idsede\n" +
            "                       where a.idusuario =?1 and e.nombre like %?2%  group by b.idfuncion,b.horainicio,c.idobras,c.nombre,e.nombre;")
    List<DTOHistorial> ObtenerHistorialporSede(Integer id,String nombresede);

    @Query(nativeQuery = true, value = "Select b.idfuncion as idfuncion,count(a.idboleto) as cantidad,b.costo*count(a.idboleto) as costototal, MAX(a.estado) as estado \n" +
            "                        from boleto a left join funcion b on a.idfuncion=b.idfuncion\n" +
            "                                   left join obras c on b.obras_idobras = c.idobras\n" +
            "                                   left join sala d on b.idsala = d.idsala\n" +
            "                                    left join sede e on d.idsede = e.idsede\n" +
            "                                   where a.idusuario =?1 and a.estado=?2 group by b.idfuncion,b.horainicio,c.idobras,c.nombre,e.nombre;")
    List<DTOHistorial> ObtenerHistorialporEstado(Integer id,int estado);

    @Query(nativeQuery = true, value = "select f.fecha, f.horainicio, b.idboleto from funcion f \n" +
            "            inner join boleto b on f.idfuncion = b.idfuncion\n" +
            "            inner join usuario u on b.idusuario = u.idusuario where u.idusuario=?1")
    List<DTOObtenerHoraFuncion>  ObtenerByfuncion(Integer idusuario);

    @Query(nativeQuery = true, value = "Select b.idfuncion as idfuncion,count(a.idboleto) as cantidad,b.costo*count(a.idboleto) as costototal, MAX(a.estado) as estado " +
            "from boleto a left join funcion b on a.idfuncion=b.idfuncion\n" +
            "            left join obras c on b.obras_idobras = c.idobras\n" +
            "            left join sala d on b.idsala = d.idsala\n" +
            "            left join sede e on d.idsede = e.idsede\n" +
            "            where a.idusuario =?1 and b.fecha >= ?2 and b.fecha <= ?3 group by b.idfuncion,b.horainicio,c.idobras,c.nombre,e.nombre;")
    List<DTOHistorial> ObtenerHistorialporFecha(Integer id,String fecha1,String fecha2);



}
