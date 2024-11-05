package com.example.demo.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Imagen;
import com.example.demo.service.ImagenService;

@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @GetMapping
    public List<Imagen> getAllImagenes() {
        return imagenService.getAllImagenes();
    }

    @GetMapping("/{id}")
    public Optional<Imagen> getImagenById(@PathVariable Long id) {
        return imagenService.getImagenById(id);
    }

    @PostMapping
    public Imagen createImagen(@RequestBody Imagen imagen) {
        return imagenService.saveImagen(imagen);
    }

    @PutMapping("/{id}")
    public Imagen updateImagen(@PathVariable Long id, @RequestBody Imagen imagen) {
        Optional<Imagen> existingImagen = imagenService.getImagenById(id);
        if (existingImagen.isPresent()) {
            Imagen updatedImagen = existingImagen.get();
            updatedImagen.setNombre(imagen.getNombre());
            updatedImagen.setHabitacion(imagen.getHabitacion());
            return imagenService.saveImagen(updatedImagen);
        } else {
            return null; 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImagen(@PathVariable Long id) {
        imagenService.deleteImagen(id);
        return ResponseEntity.ok("Imagen eliminada con éxito");
    }
}
