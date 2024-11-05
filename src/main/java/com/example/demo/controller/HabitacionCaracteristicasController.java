package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.HabitacionCaracteristicas;
import com.example.demo.service.HabitacionCaracteristicasService;

@RestController
@RequestMapping("/caracteristicashabitaciones")
public class HabitacionCaracteristicasController {

    @Autowired
    private HabitacionCaracteristicasService service;

    @PostMapping
    public HabitacionCaracteristicas agregarCaracteristica(@RequestBody HabitacionCaracteristicas caracteristica) {
        return service.agregarCaracteristica(caracteristica);
    }

    @GetMapping
    public List<HabitacionCaracteristicas> obtenerTodasLasCaracteristicas() {
        return service.obtenerTodasLasCaracteristicas();
    }
}


