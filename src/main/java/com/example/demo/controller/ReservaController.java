package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.RangoFechaReservaDto;
import com.example.demo.dto.ReservaDTO;
import com.example.demo.model.Cuenta;
import com.example.demo.model.Habitacion;
import com.example.demo.model.Reserva;
import com.example.demo.repository.HabitacionRepository;
import com.example.demo.service.CuentaService;
import com.example.demo.service.EmailService;
import com.example.demo.service.HabitacionService;
import com.example.demo.service.ReservaService;

@RestController
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private HabitacionService habitacionService;

    @GetMapping("/fechas-no-disponibles/habitacion/{habitacionId}")
    public List<RangoFechaReservaDto> obtenerFechasNoDisponibles(@PathVariable Long habitacionId) {
        return reservaService.obtenerFechasNoDisponibles(habitacionId);
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<String> verificarDisponibilidad(
            @RequestParam Long habitacionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        boolean disponible = reservaService.verificarDisponibilidad(habitacionId, fechaInicio, fechaFin);
        if (disponible) {
            return ResponseEntity.ok("La habitación está disponible");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La habitación no está disponible para el rango de fechas seleccionado");
        }
    }

    @GetMapping("/todas")
    public ResponseEntity<List<Reserva>> obtenerTodasReservas() {
        List<Reserva> reservas = reservaService.obtenerTodasReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Reserva>> obtenerReservasPorCliente(@PathVariable Long clienteId) {
        List<Reserva> reservas = reservaService.obtenerReservasPorCliente(clienteId);
        return ResponseEntity.ok(reservas);
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearReserva(@RequestBody ReservaDTO reservaDTO) {
        Reserva nuevaReserva = new Reserva();

        // Busca la instancia de Habitacion
        Habitacion habitacion = habitacionRepository.findById(reservaDTO.getHabitacionId())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        nuevaReserva.setHabitacion(habitacion); // Asigna la instancia de Habitacion
        nuevaReserva.setFechaReserva(reservaDTO.getFechaReserva());
        nuevaReserva.setFechaFinReserva(reservaDTO.getFechaFinReserva()); // Asigna fecha de fin
        nuevaReserva.setClienteId(reservaDTO.getClienteId());

        Reserva reserva = reservaService.guardarReserva(nuevaReserva);

        Cuenta cuenta = cuentaService.findById(reservaDTO.getClienteId());
        Habitacion habitacionReservada = habitacionService.findById(reservaDTO.getHabitacionId());

        try {
            emailService.enviarCorreo(
                    reserva,
                    cuenta.getCorreo(),
                    cuenta.getNombre(),
                    habitacionReservada,
                    reservaDTO.getFechaReserva(),
                    reservaDTO.getFechaFinReserva());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (reserva != null && !reserva.equals("Fecha no disponible")) {
            return ResponseEntity.ok("Reserva creada con éxito");
        }
        return ResponseEntity.ok("Fecha no disponible");
    }

    @GetMapping("/validar-disponibilidad")
    public ResponseEntity<String> validarDisponibilidad(
            @RequestParam Long habitacionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        boolean disponible = reservaService.verificarDisponibilidad(habitacionId, fechaInicio, fechaFin);

        if (disponible) {
            return ResponseEntity.ok("Disponible");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La habitación no está disponible para el rango de fechas seleccionado");
        }
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Reserva>> obtenerHistorial(@RequestParam Long clienteId) {
        List<Reserva> historial = reservaService.obtenerHistorialReservas(clienteId);
        if (historial.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmarReserva(@RequestParam Long reservaId) {
        String resultado = reservaService.confirmarReserva(reservaId);

        if ("Reserva no encontrada.".equals(resultado)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        } else {
            return ResponseEntity.ok("Reserva confirmada con éxito");
        }
    }

}
    