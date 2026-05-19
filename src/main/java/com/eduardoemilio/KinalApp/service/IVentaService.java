package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {
     List<Venta> listarVenta();
     List<Venta> listarEstadoVenta(int estado);
    Venta guardar(Venta venta);
     Optional<Venta> buscarPorCode(int code);
     Venta ActualizarV(int code, Venta venta);
     void eliminarV(int code);
     boolean existCodeV(int code);
    Venta actualizar(Venta venta);
}
