package com.mimesis.repository;

import com.mimesis.dto.ClientesporSedeDTO;
import com.mimesis.dto.DTOHistorial;
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


    @Query(nativeQuery = true, value = "Select c.nombre as nombreobra, b.fecha as fecha, b.horainicio as horainicio,b.horafin as horafin, " +
            "count(a.idboleto) as cantidad,e.nombre as nombresede,d.nombre as nombresala, a.estado as estado from boleto a left join funcion b on a.idfuncion=b.idfuncion\n" +
            "            left join obras c on b.obras_idobras = c.idobras\n" +
            "            left join sala d on b.idsala = d.idsala\n" +
            "            left join sede e on d.idsede = e.idsede\n" +
            "            where a.idusuario =?1 group by b.idfuncion,b.horainicio,c.idobras,c.nombre,e.nombre")
    List<DTOHistorial> ObtenerHistorial(Integer id);

    @Query(nativeQuery = true,value = "SELECT se.* FROM funcion f inner join boleto b on b.idfuncion = f.idfuncion \n" +
            "inner join obras o on o.idobras = f.obras_idobras inner join sala s on s.idsala = f.idsala\n" +
            "inner join sede se on se.idsede=s.idsede where b.idusuario =?1;")
    List<Sede> listaSedesporUsuario(Integer id);

    @Query(nativeQuery = true,value = "SELECT o.* FROM funcion f inner join boleto b on b.idfuncion = f.idfuncion \n" +
            "inner join obras o on o.idobras = f.obras_idobras inner join sala s on s.idsala = f.idsala\n" +
            "inner join sede se on se.idsede=s.idsede where b.idusuario =?1;")
    List<Obra> listaObrasporUsuario(Integer id);

    @Query(nativeQuery = true,value = "SELECT b.* FROM funcion f inner join boleto b on b.idfuncion = f.idfuncion \n" +
            "inner join obras o on o.idobras = f.obras_idobras inner join sala s on s.idsala = f.idsala\n" +
            "inner join sede se on se.idsede=s.idsede where b.idusuario =?1;")
    List<Boleto> listaBoletosporUsuario(Integer id);



}
