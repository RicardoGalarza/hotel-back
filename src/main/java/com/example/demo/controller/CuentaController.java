package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CuentaLoginDto;
import com.example.demo.dto.LoginResponse;
import com.example.demo.model.Cuenta;
import com.example.demo.service.CuentaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cuentas")
@CrossOrigin(origins = "http://localhost:3000")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    public List<Cuenta> getAllCuentas() {
        return cuentaService.getAllCuentas();
    }

    @GetMapping("/{id}")
    public Optional<Cuenta> getCuentaById(@PathVariable Long id) {
        return cuentaService.getCuentaById(id);
    }

    @PostMapping
    public Cuenta createCuenta(@Valid @RequestBody Cuenta cuenta) {
        return cuentaService.saveCuenta(cuenta);
    }

    @PutMapping("/{id}")
    public Cuenta updateCuenta(@PathVariable Long id, @RequestBody Cuenta cuenta) {
        Optional<Cuenta> existingCuenta = cuentaService.getCuentaById(id);
        if (existingCuenta.isPresent()) {
            Cuenta updatedCuenta = existingCuenta.get();
            updatedCuenta.setUsuario(cuenta.getUsuario());
            updatedCuenta.setCorreo(cuenta.getCorreo());
            updatedCuenta.setClave(cuenta.getClave());
            updatedCuenta.setNombre(cuenta.getNombre());
            updatedCuenta.setApellido(cuenta.getApellido());
            return cuentaService.saveCuenta(updatedCuenta);
        } else {
            return null;
        }
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<String> actualizarRol(@PathVariable Long id, @RequestBody Map<String, Long> rol) {
        Long rolId = rol.get("rolId"); // Asegúrate de que el JSON enviado tiene una propiedad "rolId"
        cuentaService.actualizarRol(id, rolId);
        return ResponseEntity.ok("Rol actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCuenta(@PathVariable Long id) {
        cuentaService.deleteCuenta(id);
        return ResponseEntity.ok("Cuenta eliminada con éxito");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateCuenta(@RequestBody CuentaLoginDto cuentaLoginDto) {
        Optional<Long> cuentaId = cuentaService.authenticateCuenta(cuentaLoginDto.getCorreo(),
                cuentaLoginDto.getClave());

        if (cuentaId.isPresent()) {
            LoginResponse response = new LoginResponse(cuentaId.get(), "Login exitoso");
            return ResponseEntity.ok(response);
        } else {
            LoginResponse response = new LoginResponse(null, "No se encuentra la cuenta");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearCuenta(@RequestBody Cuenta nuevaCuenta, @RequestParam Long rolId) {
        try {
            // Aquí llamas al servicio para crear la cuenta
            Cuenta cuentaCreada = cuentaService.crearCuenta(nuevaCuenta, rolId);
            return ResponseEntity.status(HttpStatus.CREATED).body(cuentaCreada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear la cuenta: " + e.getMessage());
        }
    }
}