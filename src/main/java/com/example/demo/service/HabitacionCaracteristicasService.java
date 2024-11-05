package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.HabitacionCaracteristicas;
import com.example.demo.repository.HabitacionCaracteristicasRepository;

@Service
public class HabitacionCaracteristicasService {

    @Autowired
    private HabitacionCaracteristicasRepository repository;

    public HabitacionCaracteristicas agregarCaracteristica(HabitacionCaracteristicas caracteristica) {
        return repository.save(caracteristica);
    }

    public List<HabitacionCaracteristicas> obtenerTodasLasCaracteristicas() {
        return repository.findAll();
    }
}
