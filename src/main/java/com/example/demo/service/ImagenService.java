package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Imagen;
import com.example.demo.repository.ImagenRepository;

@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    public List<Imagen> getAllImagenes() {
        return imagenRepository.findAll();
    }

    public Optional<Imagen> getImagenById(Long id) {
        return imagenRepository.findById(id);
    }

    public Imagen saveImagen(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    public void deleteImagen(Long id) {
        imagenRepository.deleteById(id);
    }
}