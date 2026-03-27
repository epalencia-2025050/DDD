package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {
    List<Producto> listarProducto();
    List<Producto> ProductoEstado(int estado);
    Producto guardar(Producto producto);
    Optional<Producto> buscarPorCode(Long code);
    Producto ActualizarP(Long code, Producto producto);
    void eliminarP(Long code);
    boolean existCode(Long code);
}
