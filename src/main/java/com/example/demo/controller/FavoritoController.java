package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CrearFavoritoDto;
import com.example.demo.model.Cuenta;
import com.example.demo.model.Favorito;
import com.example.demo.model.Habitacion;
import com.example.demo.service.CuentaService;
import com.example.demo.service.FavoritoService;
import com.example.demo.service.HabitacionService;

@RestController
@RequestMapping("/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private HabitacionService habitacionService;

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<Favorito>> obtenerFavoritosPorCuenta(@PathVariable Long cuentaId) {
        List<Favorito> favoritos = favoritoService.obtenerFavoritosPorCuenta(cuentaId);
        return ResponseEntity.ok(favoritos);
    }

    // Endpoint para agregar un favorito
    @PostMapping
    public ResponseEntity<Favorito> agregarFavorito(@RequestBody CrearFavoritoDto favoritoDto) {
        
        Cuenta cuenta = cuentaService.findById(favoritoDto.getCuentaId());
        Habitacion habitacion = habitacionService.findById(favoritoDto.getHabitacionId());

        Favorito favoritoExistente = favoritoService.buscarPorCuentaYHabitacion(favoritoDto.getCuentaId(), favoritoDto.getHabitacionId());

        if (favoritoExistente != null) {
            return ResponseEntity.badRequest().build(); // Ya existe este favorito
        }

        Favorito favorito = new Favorito();
        favorito.setCuenta(cuenta);
        favorito.setHabitacion(habitacion);
        Favorito nuevoFavorito = favoritoService.crearFavorito(favorito);
        return ResponseEntity.ok(nuevoFavorito);
    }

    // Endpoint para eliminar un favorito
    @DeleteMapping
    public ResponseEntity<Void> eliminarFavorito(@RequestBody CrearFavoritoDto favoritoDto) {
        Favorito favorito = favoritoService.buscarPorCuentaYHabitacion(favoritoDto.getCuentaId(), favoritoDto.getHabitacionId());
        if (favorito != null) {
            favoritoService.eliminarFavorito(favorito);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}