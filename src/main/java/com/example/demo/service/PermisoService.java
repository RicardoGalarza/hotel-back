package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PermisoDto;
import com.example.demo.repository.PermisoRepository;
import java.util.stream.Collectors;

@Service
public class PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    public List<PermisoDto> findAllPermisos() {
        return permisoRepository.findAll().stream()
                .map(permiso -> new PermisoDto(permiso.getId(), permiso.getNombre()))
                .collect(Collectors.toList());
    }
}