package com.example.demo.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Opinion;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Long> {

    @EntityGraph(attributePaths = {"cuenta", "habitacion"})
    Optional<Opinion> findById(Long id);
    List<Opinion> findByHabitacionId(Long habitacionId);
}

