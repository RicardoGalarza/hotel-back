package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "caracteristica")
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // Nuevo campo para almacenar el nombre de la imagen
    @Column(name = "imagen_nombre")
    private String imagenNombre;

    @OneToMany(mappedBy = "caracteristica")
    @JsonIgnore
    private List<HabitacionCaracteristicas> habitacionCaracteristicas = new ArrayList<>();

    


    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return String return the imagenNombre
     */
    public String getImagenNombre() {
        return imagenNombre;
    }

    /**
     * @param imagenNombre the imagenNombre to set
     */
    public void setImagenNombre(String imagenNombre) {
        this.imagenNombre = imagenNombre;
    }

    /**
     * @return List<HabitacionCaracteristicas> return the habitacionCaracteristicas
     */
    public List<HabitacionCaracteristicas> getHabitacionCaracteristicas() {
        return habitacionCaracteristicas;
    }

    /**
     * @param habitacionCaracteristicas the habitacionCaracteristicas to set
     */
    public void setHabitacionCaracteristicas(List<HabitacionCaracteristicas> habitacionCaracteristicas) {
        this.habitacionCaracteristicas = habitacionCaracteristicas;
    }

}
