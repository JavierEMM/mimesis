package com.mimesis.repository;

import com.mimesis.entity.Foto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Integer> {
    @Query(nativeQuery = true,value = "SELECT * FROM fotos WHERE idobras = ?1")
    List<Foto> listaFotos(int idobras);
    @Query(nativeQuery = true,value = "SELECT * FROM fotos WHERE idsede = ?1")
    List<Foto> listaFotosxSede(int idsede);

    @Query(nativeQuery = true,value = "SELECT * FROM fotos WHERE idobras = ?1")
    List<Foto> listaFotosxActor(int idobras);
}
