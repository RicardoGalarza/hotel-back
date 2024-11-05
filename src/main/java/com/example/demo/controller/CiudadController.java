package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Ciudad;
import com.example.demo.service.CiudadService;

@RestController
@RequestMapping("/ciudades")
public class CiudadController {

    @Autowired
    private CiudadService ciudadService;

    @PostMapping
    public ResponseEntity<Ciudad> crearCiudad(@RequestBody Ciudad ciudad) {
        Ciudad nuevaCiudad = ciudadService.crearCiudad(ciudad);
        return new ResponseEntity<>(nuevaCiudad, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> obtenerCiudadPorId(@PathVariable Long id) {
        Optional<Ciudad> ciudad = ciudadService.obtenerCiudadPorId(id);
        return ciudad.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Ciudad>> obtenerTodasLasCiudades() {
        List<Ciudad> ciudades = ciudadService.obtenerTodasLasCiudades();
        return new ResponseEntity<>(ciudades, HttpStatus.OK);
    }
}
