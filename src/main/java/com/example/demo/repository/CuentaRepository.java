package com.example.demo.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByUsuario(String usuario);
    Optional<Cuenta> findByCorreo(String correo);
    Cuenta findByCorreoAndClave(String correo, String clave);
}
