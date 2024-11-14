package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

import com.example.demo.model.Habitacion;
import com.example.demo.model.Imagen;
import com.example.demo.service.GoogleCloudStorageService;
import com.example.demo.service.ImagenService;

@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

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

    @PostMapping("/subir")
    public String uploadImagen(@RequestParam("file") MultipartFile file,
            @RequestParam("habitacionId") Long habitacionId) throws IOException {
        // Subir la imagen a GCP
        String fileName = file.getOriginalFilename();
        String imageUrl = googleCloudStorageService.uploadFile(file, fileName);

        // Guardar información de la imagen en la base de datos
        Imagen imagen = new Imagen();
        imagen.setNombre(fileName);

        // Usa el setter correcto para asociar la imagen con una habitación
        Habitacion habitacion = new Habitacion();
        habitacion.setId(habitacionId); // Se supone que habrás cargado el habitacion con el id adecuado
        imagen.setHabitacion(habitacion); // Relaciona la imagen con la habitación

        imagen.setUrl(imageUrl); // Establece la URL de la imagen

        imagenService.saveImagen(imagen); // Guarda la imagen en la base de datos

        return "Imagen subida correctamente. URL: " + imageUrl;
    }

    @GetMapping("/descargar/{fileName}")
public ResponseEntity<byte[]> downloadImagen(@PathVariable String fileName) throws IOException {
    // Llamar al método downloadFile para obtener los bytes de la imagen
    byte[] imageBytes = googleCloudStorageService.downloadFile(fileName);

    // Establecer las cabeceras para indicar que el contenido es una imagen
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");  // Cambiar a otro tipo si la imagen no es JPEG
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");

    return ResponseEntity.ok().headers(headers).body(imageBytes);
}
}