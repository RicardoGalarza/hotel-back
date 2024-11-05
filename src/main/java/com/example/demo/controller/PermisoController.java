package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.PermisoService;
import com.example.demo.dto.PermisoDto;

@RestController
@RequestMapping("/permisos")
public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @GetMapping
    public ResponseEntity<List<PermisoDto>> getAllPermisos() {
        List<PermisoDto> permisos = permisoService.findAllPermisos();
        return ResponseEntity.ok(permisos);
    }
}