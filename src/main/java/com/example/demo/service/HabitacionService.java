package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Caracteristica;
import com.example.demo.model.Habitacion;
import com.example.demo.repository.HabitacionCaracteristicasRepository;
import com.example.demo.repository.HabitacionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private HabitacionCaracteristicasRepository habitacionCaracteristicasRepository;

    public Habitacion crearHabitacion(Habitacion habitacion) {
        try {
            return habitacionRepository.save(habitacion);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("El nombre de la habitación ya existe");
        }
    }

    public List<Habitacion> getAllHabitaciones() {
        return habitacionRepository.findAll();
    }

    public Optional<Habitacion> getHabitacionById(Long id) {
        return habitacionRepository.findById(id);
    }

    public Habitacion saveHabitacion(Habitacion habitacion) {
        return habitacionRepository.save(habitacion);
    }

    public void deleteHabitacion(Long id) {
        habitacionRepository.deleteById(id);
    }

    public List<Habitacion> getHabitacionByCategoriaId(Long id) {
        return habitacionRepository.findByCategorias_Id(id);
    }

    public Habitacion findById(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Habitación no encontrada con id: " + id));
    }

    public List<Habitacion> findByCiudadId(Long idCiudad) {
        return habitacionRepository.findByCiudad_Id(idCiudad);
    }

    public List<Habitacion> filtrarHabitaciones(Long destino, List<Long> categorias, LocalDate fechaLlegada, LocalDate fechaSalida, Integer adultos, Integer ninos) {
        return habitacionRepository.filtrarHabitaciones(destino, categorias, fechaLlegada, fechaSalida, adultos, ninos);
    }
    
    

    public List<Habitacion> buscarHabitaciones(String busqueda) {
        return habitacionRepository.buscarPorNombreYDescripcion(busqueda);
    }

    public List<Caracteristica> obtenerCaracteristicasPorHabitacionId(Long habitacionId) {
        // Obtener la habitación para verificar que existe
        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada con ID: " + habitacionId));
        
        // Obtener las características a través de la tabla intermedia
        return habitacionCaracteristicasRepository.findCaracteristicasByHabitacionId(habitacionId);
    }
}