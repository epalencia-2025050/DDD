package com.eduardoemilio.KinalApp.repository;

import com.eduardoemilio.KinalApp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByEstado(int estado);

}
