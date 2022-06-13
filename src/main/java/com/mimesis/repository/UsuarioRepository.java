package com.mimesis.repository;

import com.mimesis.dto.ClientesporSedeDTO;
import com.mimesis.dto.DTOHistorial;
import com.mimesis.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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


    @Query(nativeQuery = true, value = "Select c.nombre as nombreobra, b.horainicio, count(a.idboleto) as cant,e.nombre as nombresede, a.estado from boleto a left join funcion b on a.idfuncion=b.idfuncion\n" +
            "            left join obras c on b.obras_idobras = c.idobras\n" +
            "            left join sala d on b.idsala = d.idsala\n" +
            "            left join sede e on d.idsede = e.idsede\n" +
            "            where a.idusuario =?1 group by b.idfuncion,b.horainicio,c.idobras,c.nombre,e.nombre;")
    List<DTOHistorial> ObtenerHistorial(Integer id);






}
