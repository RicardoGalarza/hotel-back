package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Favorito;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    
    // Método para buscar favoritos por cuenta y habitación
    Favorito findByCuentaIdAndHabitacionId(Long cuentaId, Long habitacionId);
    List<Favorito> findByCuentaId(Long cuentaId);
}