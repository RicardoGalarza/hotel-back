package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.example.demo.model.Caracteristica;
import com.example.demo.model.Categoria;
import com.example.demo.model.Ciudad;
import com.example.demo.model.Habitacion;
import com.example.demo.model.Imagen;
import com.example.demo.model.Politica;
import com.example.demo.repository.CiudadRepository;
import com.example.demo.service.CaracteristicaService;
import com.example.demo.service.CategoriaService;
import com.example.demo.service.HabitacionService;
import com.example.demo.service.ImagenService;
import com.example.demo.service.PoliticaService;


@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private PoliticaService politicaService;

    @Autowired
    private CaracteristicaService caractertisticaService;

    @GetMapping
    public List<Habitacion> getAllHabitaciones() {
        List<Habitacion> habitaciones = habitacionService.getAllHabitaciones();
        Collections.shuffle(habitaciones); // Mezcla la lista de forma aleatoria
        return habitaciones;
    }

    @GetMapping("/{id}")
    public Optional<Habitacion> getHabitacionById(@PathVariable Long id) {
        return habitacionService.getHabitacionById(id);
    }

    @GetMapping("/categoria/{id}")
    public List<Habitacion> getHabitacionByCategoriaId(@PathVariable Long id) {
        return habitacionService.getHabitacionByCategoriaId(id);
    }

    @GetMapping("/ciudad/{idCiudad}")
    public ResponseEntity<List<Habitacion>> getHabitacionesPorCiudad(@PathVariable Long idCiudad) {
        List<Habitacion> habitaciones = habitacionService.findByCiudadId(idCiudad);
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Habitacion> createHabitacion(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("categorias") String categorias,
            @RequestParam("imagenes") List<MultipartFile> imagenes, // Asegúrate de que las imágenes estén bien
                                                                    // recogidas
            @RequestParam("ciudad") Long idCiudad,
            @RequestParam("huespedesAdultos") int huespedesAdultos,
            @RequestParam("huespedesNinos") int huespedesNinos,
            @RequestParam("whatsapp") String whatsapp,
            @RequestParam("politicas") String politicas,
            @RequestParam("caracteristicas") String caracteristicas) {

        System.out.println("entro");
        Ciudad ciudad = ciudadRepository.findById(idCiudad)
                .orElseThrow(() -> new IllegalArgumentException("Ciudad no encontrada"));
        // Guardar la habitación y obtener su ID
        Habitacion habitacion = new Habitacion();
        habitacion.setNombre(nombre);
        habitacion.setDescripcion(descripcion);
        habitacion.setPrecio(precio);
        habitacion.setCiudad(ciudad);
        habitacion.setHuespedesAdultos(huespedesAdultos);
        habitacion.setHuespedesNinos(huespedesNinos);
        habitacion.setWhatsapp(whatsapp);

        List<Long> categoriaIds = Arrays.stream(categorias.replace("[", "").replace("]", "").split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<Categoria> categoriasObj = categoriaService.getCategoriasByIds(categoriaIds);

        habitacion.setCategorias(categoriasObj);

        // Manejar las políticas
        List<Long> politicaIds = Arrays.stream(politicas.replace("[", "").replace("]", "").split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<Politica> politicasObj = politicaService.getPoliticasByIds(politicaIds);
        habitacion.setPoliticas(politicasObj);

        // Manejar las caracteristicas
        List<Long> caracteristicasIds = Arrays.stream(caracteristicas.replace("[", "").replace("]", "").split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<Caracteristica> caracteristicasObj = caractertisticaService.getCaracteristicasByIds(caracteristicasIds);
        habitacion.setCaracteristicas(caracteristicasObj);

        Habitacion savedHabitacion = habitacionService.saveHabitacion(habitacion);
        

        // Crear el directorio basado en el ID de la habitación
        String uploadDirectory = "src/main/resources/static/uploads/" + savedHabitacion.getId() + "/";
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs(); // Crear la estructura de directorios si no existe
        }

        // Guardar las imágenes
        for (MultipartFile imagen : imagenes) {
            try {
                Path filePath = Paths.get(uploadDirectory + imagen.getOriginalFilename().replace(" ", ""));
                Files.write(filePath, imagen.getBytes());

                Imagen nuevaImagen = new Imagen();
                nuevaImagen.setNombre(imagen.getOriginalFilename().replace(" ", ""));
                nuevaImagen.setHabitacion(savedHabitacion);
                imagenService.saveImagen(nuevaImagen); // Guardar la imagen en la base de datos

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).build();
            }
        }

        return ResponseEntity.ok(savedHabitacion);
    }

    @PutMapping("/{id}/caracteristicas")
    public ResponseEntity<?> actualizarCaracteristicas(@PathVariable Long id,
            @RequestBody List<Long> caracteristicaIds) {
        boolean actualizado = caractertisticaService.actualizarCaracteristicas(id, caracteristicaIds);

        if (actualizado) {
            return ResponseEntity.ok("Características actualizadas correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Habitación no encontrada");
        }
    }

    @GetMapping("/{id}/caracteristicas")
    public List<Caracteristica> obtenerCaracteristicasPorHabitacionId(@PathVariable Long id) {
        return habitacionService.obtenerCaracteristicasPorHabitacionId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habitacion> updateHabitacion(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("categorias") String categorias,
            @RequestParam(value = "imagenes", required = false) List<MultipartFile> imagenes,
            @RequestParam("ciudad") Long idCiudad,
            @RequestParam("huespedesAdultos") int huespedesAdultos,
            @RequestParam("huespedesNinos") int huespedesNinos,
            @RequestParam("whatsapp") String whatsapp,
            @RequestParam(value = "politicas", required = false) String politicas,
            @RequestParam(value = "caracteristicas", required = false) String caracteristicas) {

        Optional<Habitacion> existingHabitacion = habitacionService.getHabitacionById(id);
        if (existingHabitacion.isPresent()) {
            Habitacion updatedHabitacion = existingHabitacion.get();

            // Actualizar los campos simples
            updatedHabitacion.setNombre(nombre);
            updatedHabitacion.setDescripcion(descripcion);
            updatedHabitacion.setPrecio(precio);
            updatedHabitacion.setHuespedesAdultos(huespedesAdultos);
            updatedHabitacion.setHuespedesNinos(huespedesNinos);
            updatedHabitacion.setWhatsapp(whatsapp);

            // Actualizar la ciudad
            Ciudad ciudad = ciudadRepository.findById(idCiudad)
                    .orElseThrow(() -> new IllegalArgumentException("Ciudad no encontrada"));
            updatedHabitacion.setCiudad(ciudad);

            // Manejar las categorías
            if (categorias != null && !categorias.isEmpty() && !categorias.equals("[]")) {
                List<Long> categoriaIds = Arrays.stream(categorias.replace("[", "").replace("]", "").split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                List<Categoria> categoriasObj = categoriaService.getCategoriasByIds(categoriaIds);
                updatedHabitacion.setCategorias(categoriasObj);
            }

            // Manejar las políticas
            if (politicas != null && !politicas.isEmpty() && !politicas.equals("[]")) {
                List<Long> politicaIds = Arrays.stream(politicas.replace("[", "").replace("]", "").split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                List<Politica> politicasObj = politicaService.getPoliticasByIds(politicaIds);
                updatedHabitacion.setPoliticas(politicasObj);
            }

            // Manejar las características
            if (caracteristicas != null && !caracteristicas.isEmpty() && !caracteristicas.equals("[]")) {
                List<Long> caracteristicaIds = Arrays
                        .stream(caracteristicas.replace("[", "").replace("]", "").split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                List<Caracteristica> caracteristicasObj = caractertisticaService
                        .getCaracteristicasByIds(caracteristicaIds);
                updatedHabitacion.setCaracteristicas(caracteristicasObj);
            }

            habitacionService.saveHabitacion(updatedHabitacion); // Guardar la habitación actualizada
            return ResponseEntity.ok(updatedHabitacion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHabitacion(@PathVariable Long id) {
        habitacionService.deleteHabitacion(id);
        return ResponseEntity.ok("Habitación eliminada con éxito");
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Habitacion>> filtrarHabitaciones(
            @RequestParam(required = false) Long destino,
            @RequestParam(required = false) List<Long> categorias,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaLlegada,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaSalida,
            @RequestParam(required = false) Integer adultos,
            @RequestParam(required = false) Integer ninos) {
        List<Habitacion> habitaciones = habitacionService.filtrarHabitaciones(destino, categorias, fechaLlegada,
                fechaSalida, adultos, ninos);
        return ResponseEntity.ok(habitaciones);
    }

    @GetMapping("/buscar")
    public List<Habitacion> buscarHabitaciones(@RequestParam String busqueda) {
        return habitacionService.buscarHabitaciones(busqueda);
    }
    @GetMapping("/uploads/{habitacionId}/{imagen}")
    public ResponseEntity<Resource> getImagen(@PathVariable Long habitacionId, @PathVariable String imagen) {
        Path path = Paths.get("src/main/resources/static/uploads/" + habitacionId + "/" + imagen);
        Resource resource = new FileSystemResource(path);
        
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok().body(resource);
    }
}
