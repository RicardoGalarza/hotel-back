package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Caracteristica;
import com.example.demo.model.HabitacionCaracteristicas;

@Repository
public interface HabitacionCaracteristicasRepository extends JpaRepository<HabitacionCaracteristicas, Long> {
    @Query("SELECT hc.caracteristica FROM HabitacionCaracteristicas hc WHERE hc.habitacion.id = :habitacionId")
    List<Caracteristica> findCaracteristicasByHabitacionId(Long habitacionId);
}
