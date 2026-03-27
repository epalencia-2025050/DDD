package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.DetalleVenta;

import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {
    List<DetalleVenta> listarDVenta();
    DetalleVenta guardar(DetalleVenta detalleVenta);
    Optional<DetalleVenta> buscarPorCode(Long code);
    DetalleVenta actualizarD(Long code, DetalleVenta detalleVenta);
    void eliminarD(Long code);
    boolean existeCode(Long code);

}
