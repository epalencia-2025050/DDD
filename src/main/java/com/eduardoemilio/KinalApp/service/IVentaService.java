package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {
     List<Venta> listarVenta();
     List<Venta> listarEstadoVenta(int estado);
    Venta guardar(Venta venta);
     Optional<Venta> buscarPorCode(Long code);
     Venta ActualizarV(Long code, Venta venta);
     void eliminarV(Long code);
     boolean existCodeV(Long code);
}
