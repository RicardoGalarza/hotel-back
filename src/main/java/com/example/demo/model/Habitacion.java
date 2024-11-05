package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Pattern;

@Entity
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    private Double precio;

    @Column(nullable = false)
    private int huespedesAdultos;

    @Column(nullable = false)
    private int huespedesNinos;

    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "categoria_habitacion", joinColumns = @JoinColumn(name = "habitacion_id"), inverseJoinColumns = @JoinColumn(name = "categoria_id"))
    private List<Categoria> categorias;


    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes;

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Opinion> opiniones;

    @ManyToMany
    @JoinTable(
        name = "habitacion_politica",
        joinColumns = @JoinColumn(name = "habitacion_id"),
        inverseJoinColumns = @JoinColumn(name = "politica_id")
    )
    private List<Politica> politicas;

    @Pattern(regexp = "^\\d{11,15}$", message = "El número de WhatsApp debe tener un formato válido, como 56962765086")
    private String whatsapp;

    @OneToMany(mappedBy = "habitacion")
    @JsonIgnore
    private List<HabitacionCaracteristicas> habitacionCaracteristicas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "habitacion_caracteristicas",
        joinColumns = @JoinColumn(name = "habitacion_id"),
        inverseJoinColumns = @JoinColumn(name = "caracteristica_id")
    )
    private List<Caracteristica> caracteristicas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getHuespedesAdultos() {
        return huespedesAdultos;
    }

    public void setHuespedesAdultos(int huespedesAdultos) {
        this.huespedesAdultos = huespedesAdultos;
    }

    public int getHuespedesNinos() {
        return huespedesNinos;
    }

    public void setHuespedesNinos(int huespedesNinos) {
        this.huespedesNinos = huespedesNinos;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public List<Opinion> getOpiniones() {
        return opiniones;
    }

    public void setOpiniones(List<Opinion> opiniones) {
        this.opiniones = opiniones;
    }

    public List<Politica> getPoliticas() {
        return politicas;
    }

    public void setPoliticas(List<Politica> politicas) {
        this.politicas = politicas;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public List<HabitacionCaracteristicas> getHabitacionCaracteristicas() {
        return habitacionCaracteristicas;
    }

    public void setHabitacionCaracteristicas(List<HabitacionCaracteristicas> habitacionCaracteristicas) {
        this.habitacionCaracteristicas = habitacionCaracteristicas;
    }

    public List<Caracteristica> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(List<Caracteristica> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }






}
