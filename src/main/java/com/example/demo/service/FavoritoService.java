package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Favorito;
import com.example.demo.repository.FavoritoRepository;

@Service
public class FavoritoService {
    
    @Autowired
    private FavoritoRepository favoritoRepository;

    public Favorito crearFavorito(Favorito favorito) {
        return favoritoRepository.save(favorito);
    }

    public List<Favorito> obtenerFavoritosPorCuenta(Long cuentaId) {
        return favoritoRepository.findByCuentaId(cuentaId);
    }


    public void eliminarFavorito(Favorito favorito) {
        favoritoRepository.delete(favorito);
    }

    public Favorito buscarPorCuentaYHabitacion(Long cuentaId, Long habitacionId) {
        return favoritoRepository.findByCuentaIdAndHabitacionId(cuentaId, habitacionId);
    }
}