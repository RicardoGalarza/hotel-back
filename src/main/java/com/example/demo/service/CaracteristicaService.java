package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Caracteristica;
import com.example.demo.model.Habitacion;
import com.example.demo.model.HabitacionCaracteristicas;
import com.example.demo.repository.CaracteristicaRepository;
import com.example.demo.repository.HabitacionRepository;

@Service
public class CaracteristicaService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    // Método para obtener todas las características
    public List<Caracteristica> obtenerTodasLasCaracteristicas() {
        return caracteristicaRepository.findAll();
    }

    // Método para crear una nueva característica
    public Caracteristica crearCaracteristica(Caracteristica caracteristica) {
        return caracteristicaRepository.save(caracteristica);
    }

    public boolean actualizarCaracteristicas(Long habitacionId, List<Long> caracteristicaIds) {
        Optional<Habitacion> habitacionOpt = habitacionRepository.findById(habitacionId);

        if (habitacionOpt.isPresent()) {
            Habitacion habitacion = habitacionOpt.get();

            // Busca las características por sus IDs
            List<Caracteristica> caracteristicas = caracteristicaRepository.findAllById(caracteristicaIds);

            // Limpia las características actuales y agrega las nuevas
            habitacion.getHabitacionCaracteristicas().clear();
            for (Caracteristica caracteristica : caracteristicas) {
                HabitacionCaracteristicas habitacionCaracteristica = new HabitacionCaracteristicas();
                habitacionCaracteristica.setHabitacion(habitacion);
                habitacionCaracteristica.setCaracteristica(caracteristica);
                habitacion.getHabitacionCaracteristicas().add(habitacionCaracteristica);
            }

            // Guarda los cambios
            habitacionRepository.save(habitacion);
            return true;
        } else {
            return false;
        }
    }

    public List<Caracteristica> getCaracteristicasByIds(List<Long> ids) {
        return caracteristicaRepository.findAllById(ids);
    }
}
