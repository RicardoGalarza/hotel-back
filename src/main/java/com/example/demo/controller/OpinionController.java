package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CrearOpinionDto;
import com.example.demo.model.Cuenta;
import com.example.demo.model.Habitacion;
import com.example.demo.model.Opinion;
import com.example.demo.service.CuentaService;
import com.example.demo.service.HabitacionService;
import com.example.demo.service.OpinionService;

@RestController
@RequestMapping("/opiniones")
public class OpinionController {

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<Opinion> crearOpinion(@RequestBody CrearOpinionDto opinionDTO) {
        Habitacion habitacion = habitacionService.findById(opinionDTO.getHabitacionId());
        Cuenta cuenta = cuentaService.findById(opinionDTO.getCuentaId());

        Opinion nuevaOpinion = new Opinion();
        nuevaOpinion.setHabitacion(habitacion);
        nuevaOpinion.setCuenta(cuenta);
        nuevaOpinion.setEstrellas(opinionDTO.getEstrellas());
        nuevaOpinion.setOpinion(opinionDTO.getOpinion());

        Opinion opinionGuardada = opinionService.crearOpinion(nuevaOpinion);

        return ResponseEntity.status(HttpStatus.CREATED).body(opinionGuardada);
    }

    @GetMapping("/habitacion/{habitacionId}")
    public List<Opinion> obtenerOpinionesPorHabitacion(@PathVariable Long habitacionId) {
        return opinionService.obtenerOpinionesPorHabitacion(habitacionId);
    }
    


}