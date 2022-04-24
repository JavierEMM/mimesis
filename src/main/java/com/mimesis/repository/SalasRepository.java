package com.mimesis.repository;

import com.mimesis.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalasRepository extends JpaRepository<Sala,Integer> {
}
