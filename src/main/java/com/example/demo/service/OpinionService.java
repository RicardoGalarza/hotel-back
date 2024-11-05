package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Opinion;
import com.example.demo.repository.OpinionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OpinionService {

    @Autowired
    private OpinionRepository opinionRepository;

    public Opinion crearOpinion(Opinion opinion) {
        // Opinion op = opinionRepository.save(opinion);
        // return this.findById(op.getId());
        return opinionRepository.save(opinion);
    }

    // Método para obtener la opinión con solo las relaciones especificadas
    public Opinion getOpinionById(Long id) {
        return opinionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opinion not found"));
    }

    public Opinion findById(Long id) {
        return opinionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Opinión no encontrada con id: " + id));
    }

    public List<Opinion> obtenerOpinionesPorHabitacion(Long habitacionId) {
        return opinionRepository.findByHabitacionId(habitacionId);
    }
}