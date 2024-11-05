package com.example.demo.dto;

import java.time.LocalDate;

public class ReservaDTO {
    private Long habitacionId;
    private Long clienteId;
    private LocalDate fechaReserva;
    private LocalDate fechaFinReserva;

    public LocalDate getFechaFinReserva() {
        return fechaFinReserva;
    }
    public void setFechaFinReserva(LocalDate fechaFinReserva) {
        this.fechaFinReserva = fechaFinReserva;
    }
    public Long getHabitacionId() {
        return habitacionId;
    }
    public void setHabitacionId(Long habitacionId) {
        this.habitacionId = habitacionId;
    }
    public LocalDate getFechaReserva() {
        return fechaReserva;
    }
    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
    public Long getClienteId() {
        return clienteId;
    }
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

}
