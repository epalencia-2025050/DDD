package com.eduardoemilio.KinalApp.repository;

import com.eduardoemilio.KinalApp.entity.Producto;
import com.eduardoemilio.KinalApp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> { ;
    List<Producto> findByEstado(int estado);
}
