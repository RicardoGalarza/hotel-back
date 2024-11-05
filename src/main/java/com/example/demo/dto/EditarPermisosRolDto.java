package com.example.demo.dto;

import java.util.List;

public class EditarPermisosRolDto {
    private List<Long> permisoIds;

    // Getters y setters
    public List<Long> getPermisoIds() {
        return permisoIds;
    }

    public void setPermisoIds(List<Long> permisoIds) {
        this.permisoIds = permisoIds;
    }
}
