package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

        List<Reserva> findByHabitacionId(Long habitacionId);

        @Query("SELECT r FROM Reserva r WHERE r.habitacion.id = :habitacionId AND " +
                        "(r.fechaReserva <= :fechaFin AND r.fechaFinReserva >= :fechaInicio)")
        List<Reserva> findByHabitacionIdAndFechas(@Param("habitacionId") Long habitacionId,
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFin") LocalDate fechaFin);

        List<Reserva> findByClienteId(Long clienteId);

        List<Reserva> findByClienteIdOrderByFechaReserva(Long clienteId);
}
