package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private LocalDateTime fechaCreacion;

    private String nombreImagen;

    @ManyToMany(mappedBy = "categorias")
    @JsonIgnore
    private List<Habitacion> habitaciones;

    


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
     * @return String return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return LocalDateTime return the fechaCreacion
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @param fechaCreacion the fechaCreacion to set
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return String return the nombreImagen
     */
    public String getNombreImagen() {
        return nombreImagen;
    }

    /**
     * @param nombreImagen the nombreImagen to set
     */
    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    /**
     * @return List<Habitacion> return the habitaciones
     */
    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    /**
     * @param habitaciones the habitaciones to set
     */
    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }

}
