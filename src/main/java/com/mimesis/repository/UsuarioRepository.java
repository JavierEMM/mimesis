package com.mimesis.repository;

import com.mimesis.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    List<Usuario> findByRol(String rol);

    Usuario findByCorreo(String correo);
}
