package com.example.demo.repository;

import com.example.demo.model.Politica;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliticaRepository extends JpaRepository<Politica, Long> {
    List<Politica> findByHabitacionesId(Long habitacionId);
}