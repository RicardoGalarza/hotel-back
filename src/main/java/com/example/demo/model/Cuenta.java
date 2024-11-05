package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 2, max = 30, message = "El nombre debe tener entre 2 y 30 caracteres")
    private String usuario;

    @Column(nullable = false, unique = true)
    @NotNull(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String correo;

    @NotNull(message = "La clave no puede ser nula")
    @Size(min = 8, max = 30, message = "La clave debe tener entre 8 y 30 caracteres")
    private String clave;

    @Column(nullable = false)
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 2, max = 30, message = "El nombre debe tener entre 2 y 30 caracteres")
    private String nombre;

    @Column(nullable = false)
    @NotNull(message = "El apellido no puede ser nulo")
    @Size(min = 2, max = 30, message = "El apellido debe tener entre 2 y 30 caracteres")
    private String apellido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    @JsonIgnoreProperties("cuentas")
    private Rol rol;

    @OneToMany(mappedBy = "cuenta")
    @JsonIgnore
    private List<Opinion> opiniones;

    public List<Opinion> getOpiniones() {
        return this.opiniones;
    }

    public void setOpiniones(List<Opinion> opiniones) {
        this.opiniones = opiniones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

}
