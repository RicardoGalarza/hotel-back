package com.example.demo.controller;

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
import com.example.demo.service.GoogleCloudStorageService;

@RestController
@RequestMapping("/caracteristicas")
public class CaracteristicaController {

    @Autowired
    private CaracteristicaService caracteristicaService;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;


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

        try {
            if (imagen != null && !imagen.isEmpty()) {
                // Generar el nombre de archivo para la imagen de la característica
                String fileName = "caracteristica-" + nombre.replaceAll("\\s+", "-") + "-" + imagen.getOriginalFilename().replace(" ", "");
                String imagePath = "caracteristicas/" + fileName;
    
                // Subir la imagen a Google Cloud Storage
                String publicUrl = googleCloudStorageService.uploadFile(imagen, imagePath);
    
                // Guardar la URL pública de la imagen en la entidad
                caracteristica.setImagenNombre(publicUrl);
            }    
        } catch (Exception e) {
        }
        // Guardar la característica y devolverla
        return caracteristicaService.crearCaracteristica(caracteristica);
    }

}
