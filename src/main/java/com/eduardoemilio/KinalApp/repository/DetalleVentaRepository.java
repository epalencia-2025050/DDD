package com.eduardoemilio.KinalApp.repository;

import com.eduardoemilio.KinalApp.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVenta_CodigoVenta(Long codigoVenta);
}
