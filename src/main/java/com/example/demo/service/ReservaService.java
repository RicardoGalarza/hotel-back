package com.example.demo.service;

import com.example.demo.dto.RangoFechaReservaDto;
import com.example.demo.model.Reserva;
import com.example.demo.repository.ReservaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public List<RangoFechaReservaDto> obtenerFechasNoDisponibles(Long habitacionId) {
        List<Reserva> reservas = reservaRepository.findByHabitacionId(habitacionId);
        return reservas.stream()
                .map(reserva -> new RangoFechaReservaDto(
                        reserva.getFechaReserva().toString(),
                        reserva.getFechaFinReserva().toString()
                ))
                .collect(Collectors.toList());
    }

    public boolean verificarDisponibilidad(Long habitacionId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Reserva> reservas = reservaRepository.findByHabitacionIdAndFechas(habitacionId, fechaInicio, fechaFin);
        return reservas.isEmpty(); // Retorna true si no hay reservas en ese rango
    }

    public Reserva guardarReserva(Reserva reserva) {
        boolean disponible = verificarDisponibilidad(
            reserva.getHabitacion().getId(),
            reserva.getFechaReserva(),
            reserva.getFechaFinReserva()
        );
    
        if (disponible) {
            // Guarda la reserva en la base de datos
            Reserva reservaGuardada = reservaRepository.save(reserva);
            // Retorna la reserva guardada
            return reservaGuardada;
        } else {
            // Si la fecha no está disponible, puedes lanzar una excepción o manejar el error de manera adecuada
            throw new IllegalStateException("Fecha no disponible");
        }
    }
    

    public List<Reserva> obtenerTodasReservas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> obtenerReservasPorCliente(Long clienteId) {
        return reservaRepository.findByClienteId(clienteId);
    }

    public List<Reserva> obtenerHistorialReservas(Long clienteId) {
        return reservaRepository.findByClienteIdOrderByFechaReserva(clienteId);
    }

    public String confirmarReserva(Long reservaId) {
        Optional<Reserva> reservaOptional = reservaRepository.findById(reservaId);

        if (reservaOptional.isPresent()) {
            Reserva reserva = reservaOptional.get();
            if (!reserva.isConfirmado()) {
                reserva.setConfirmado(true);
                reservaRepository.save(reserva);
                return "Reserva confirmada con éxito.";
            } else {
                return "La reserva ya estaba confirmada.";
            }
        } else {
            return "Reserva no encontrada.";
        }
    }

}
