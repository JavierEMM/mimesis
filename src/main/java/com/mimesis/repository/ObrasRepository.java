package com.mimesis.repository;

import com.mimesis.entity.Obra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObrasRepository extends JpaRepository<Obra,Integer> {
}
