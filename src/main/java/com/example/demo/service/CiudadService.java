package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Ciudad;
import com.example.demo.repository.CiudadRepository;

@Service
public class CiudadService {

    @Autowired
    private CiudadRepository ciudadRepository;

    public Ciudad crearCiudad(Ciudad ciudad) {
        return ciudadRepository.save(ciudad);
    }

    public Optional<Ciudad> obtenerCiudadPorId(Long id) {
        return ciudadRepository.findById(id);
    }

    public List<Ciudad> obtenerTodasLasCiudades() {
        return ciudadRepository.findAll();
    }
}
