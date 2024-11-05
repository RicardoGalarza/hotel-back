package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Caracteristica;
import com.example.demo.service.CaracteristicaService;

@RestController
@RequestMapping("/caracteristicas")
public class CaracteristicaController {

    @Autowired
    private CaracteristicaService caracteristicaService;

    @GetMapping
    public List<Caracteristica> obtenerTodasLasCaracteristicas() {
        return caracteristicaService.obtenerTodasLasCaracteristicas();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Caracteristica crearCaracteristica(
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setNombre(nombre);

        Caracteristica carac = null;

        // Guardar el nombre del archivo de imagen, si se proporciona una imagen
        if (imagen != null && !imagen.isEmpty()) {
            String nombreImagen = imagen.getOriginalFilename();
            caracteristica.setImagenNombre(nombreImagen);

            carac = caracteristicaService.crearCaracteristica(caracteristica);

            // Aquí puedes agregar la lógica para guardar la imagen en el servidor
            try {
                // Ruta donde se guardarán las imágenes
                String uploadDirectory = "src/main/resources/static/uploads/caracteristicas/"+carac.getId()+"/";
                File directory = new File(uploadDirectory);
                if (!directory.exists()) {
                    directory.mkdirs(); // Crear el directorio si no existe
                }

                // Guardar la imagen en el servidor
                Path filePath = Paths.get(uploadDirectory + nombreImagen);
                Files.write(filePath, imagen.getBytes());

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error al guardar la imagen", e);
            }
        }

        return carac;
    }
}
