package com.example.demo.repository;

import com.example.demo.model.Habitacion;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByCategorias_Id(Long categoriaId);

    List<Habitacion> findByCiudad_Id(Long idCiudad);

    @Query("SELECT h FROM Habitacion h "
        + "JOIN h.ciudad c "
        + "LEFT JOIN h.categorias cat "
        + "LEFT JOIN Reserva r ON r.habitacion.id = h.id "
        + "WHERE (:destino IS NULL OR c.id = :destino) "
        + "AND (:categorias IS NULL OR cat.id IN :categorias) "
        + "AND (:adultos IS NULL OR h.huespedesAdultos >= :adultos) "
        + "AND (:ninos IS NULL OR h.huespedesNinos >= :ninos) "
        + "AND (r IS NULL OR :fechaLlegada IS NULL OR :fechaSalida IS NULL OR r.fechaReserva NOT BETWEEN :fechaLlegada AND :fechaSalida)")
List<Habitacion> filtrarHabitaciones(@Param("destino") Long destino,
        @Param("categorias") List<Long> categorias,
        @Param("fechaLlegada") LocalDate fechaLlegada,
        @Param("fechaSalida") LocalDate fechaSalida,
        @Param("adultos") Integer adultos,
        @Param("ninos") Integer ninos);






    @Query("SELECT h FROM Habitacion h WHERE LOWER(h.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) "
            + "OR LOWER(h.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Habitacion> buscarPorNombreYDescripcion(String busqueda);

}
