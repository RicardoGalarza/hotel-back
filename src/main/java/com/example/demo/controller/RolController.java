package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CrearRolDto;
import com.example.demo.dto.EditarRolDto;
import com.example.demo.model.Rol;
import com.example.demo.service.RolService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping("/{id}")
    public ResponseEntity<Rol> getRolById(@PathVariable Long id) {
        Optional<Rol> rol = rolService.getRolById(id);
        return rol.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @CrossOrigin(origins = "http://hotel-back-production.up.railway.app:8080")
    public ResponseEntity<?> createRol(@Valid @RequestBody CrearRolDto rolDTO) {
        try {
            Rol rol = rolService.createRol(rolDTO);
            return ResponseEntity.ok(rol);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el rol: " + e.getMessage());
        }
    }

    @PutMapping("/{rolId}")
    public ResponseEntity<Rol> updateRol(@PathVariable Long rolId, @RequestBody EditarRolDto rolDTO) {
        Rol updatedRol = rolService.updateRol(rolId, rolDTO);
        return ResponseEntity.ok(updatedRol);
    }
}
