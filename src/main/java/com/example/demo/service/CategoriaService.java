package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.Categoria;
import com.example.demo.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }
    public Page<Categoria> obtenerCategorias(Pageable pageable) {
        return categoriaRepository.findAll(pageable); // Uso de la paginación
    }

    public Optional<Categoria> getCategoriaById(Long categoriaId) {
        return categoriaRepository.findById(categoriaId);  
    }

    public Categoria saveCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void deleteCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }


    public List<Categoria> getCategoriasByIds(List<Long> categoriaIds) {
        return categoriaRepository.findAllById(categoriaIds);
    }
    


}
