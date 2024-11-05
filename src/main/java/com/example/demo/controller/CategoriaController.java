package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Categoria;
import com.example.demo.service.CategoriaService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Categoria crearCategoria(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("imagen") MultipartFile imagen) {

        try {
            // Procesar la imagen y definir la ruta
            String uploadDirectory = "src/main/resources/static/uploads/categorias/";
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs(); // Crear el directorio si no existe
            }

            String fileName = imagen.getOriginalFilename().replace(" ", "");
            String filePath = uploadDirectory + fileName;
            Path path = Paths.get(filePath);
            Files.write(path, imagen.getBytes());

            // Crear y guardar la categoría
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            categoria.setFechaCreacion(LocalDateTime.now());
            categoria.setNombreImagen(fileName); // Asigna la ruta de la imagen

            return categoriaService.crearCategoria(categoria);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaService.obtenerCategorias();
    }

    @GetMapping("/{id}")
    public Optional<Categoria> getCategoriaById(@PathVariable Long id) {
        return categoriaService.getCategoriaById(id);
    }

    @GetMapping("/pageable")
    public Page<Categoria> listarCategorias(Pageable pageable) {
        return categoriaService.obtenerCategorias(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        Optional<Categoria> existingCategoria = categoriaService.getCategoriaById(id);

        if (existingCategoria.isPresent()) {
            Categoria updatedCategoria = existingCategoria.get();
            updatedCategoria.setNombre(categoria.getNombre());
            updatedCategoria.setDescripcion(categoria.getDescripcion());

            categoriaService.saveCategoria(updatedCategoria);
            return ResponseEntity.ok(updatedCategoria);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.ok("Categoria eliminada con éxito");
    }

}
