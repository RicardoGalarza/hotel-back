package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Politica;
import com.example.demo.repository.PoliticaRepository;

@Service
public class PoliticaService {

    private final PoliticaRepository politicaRepository;

    public PoliticaService(PoliticaRepository politicaRepository) {
        this.politicaRepository = politicaRepository;
    }

    public List<Politica> obtenerPoliticasPorHabitacion(Long habitacionId) {
        return politicaRepository.findByHabitacionesId(habitacionId);
    }

    public Politica guardarPolitica(Politica politica) {
        return politicaRepository.save(politica);
    }

    public List<Politica> obtenerTodasLasPoliticas() {
        return politicaRepository.findAll();
    }

    public List<Politica> getPoliticasByIds(List<Long> ids) {
        return politicaRepository.findAllById(ids);
    }
}
