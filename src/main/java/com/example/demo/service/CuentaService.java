package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cuenta;
import com.example.demo.model.Rol;
import com.example.demo.repository.CuentaRepository;
import com.example.demo.repository.RolRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private RolRepository rolRepository;
    public List<Cuenta> getAllCuentas() {
        return cuentaRepository.findAll();
    }

    public Optional<Cuenta> getCuentaById(Long id) {
        return cuentaRepository.findById(id);
    }

    public Cuenta saveCuenta(Cuenta cuenta) {
        Optional<Cuenta> existingCuentaByUsuario = cuentaRepository.findByUsuario(cuenta.getUsuario());
        if (existingCuentaByUsuario.isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        Optional<Cuenta> existingCuentaByCorreo = cuentaRepository.findByCorreo(cuenta.getCorreo());
        if (existingCuentaByCorreo.isPresent()) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }
        return cuentaRepository.save(cuenta);
    }

    public void deleteCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }

    public Optional<Long> authenticateCuenta(String correo, String clave) {
        Cuenta cuenta = cuentaRepository.findByCorreoAndClave(correo, clave);
        return Optional.ofNullable(cuenta).map(Cuenta::getId);
    }

    public void actualizarRol(Long id, Long rolId) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(id);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setRol(new Rol(rolId));  // Asigna el rol según el ID
            cuentaRepository.save(cuenta);
        } else {
            throw new RuntimeException("Cuenta no encontrada");
        }
    }

    public Cuenta crearCuenta(Cuenta nuevaCuenta, Long rolId) {
        // Buscar el rol por ID
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));

        // Asignar el rol a la cuenta
        System.out.println(rol.toString()); 
        nuevaCuenta.setRol(rol);

        // Guardar la cuenta en la base de datos
        return cuentaRepository.save(nuevaCuenta);
    }

    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + id));
    }
    
}
