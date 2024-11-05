package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CrearRolDto;
import com.example.demo.dto.EditarRolDto;
import com.example.demo.model.Permiso;
import com.example.demo.model.Rol;
import com.example.demo.repository.PermisoRepository;
import com.example.demo.repository.RolRepository;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    public Optional<Rol> getRolById(Long id) {
        return rolRepository.findById(id);
    }

    @Transactional
    public Rol createRol(CrearRolDto rolDTO) {
        Rol rol = new Rol();  // Ya debería funcionar correctamente
        rol.setNombre(rolDTO.getNombre());

        if (rolDTO.getPermisosIds() != null && !rolDTO.getPermisosIds().isEmpty()) {
            List<Permiso> permisos = permisoRepository.findAllById(rolDTO.getPermisosIds());
            rol.setPermisos(permisos);
        } else {
            rol.setPermisos(new ArrayList<>());
        }
        
        return rolRepository.save(rol);
    }


    @Transactional
    public Rol updateRol(Long rolId, EditarRolDto rolDTO) {
        Rol rol = rolRepository.findById(rolId)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));

        rol.setNombre(rolDTO.getNombre());
        List<Permiso> permisos = permisoRepository.findAllById(rolDTO.getPermisoIds());
        rol.setPermisos(permisos);

        return rolRepository.save(rol);
    }

}
