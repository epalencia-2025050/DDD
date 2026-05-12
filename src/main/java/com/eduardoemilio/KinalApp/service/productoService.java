package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.Producto;
import com.eduardoemilio.KinalApp.entity.Usuario;
import com.eduardoemilio.KinalApp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class productoService implements IProductoService {

    private final ProductoRepository productoRepository;

    public productoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarProducto() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> ProductoEstado(int estado) {

        return productoRepository.findByEstado(estado);
    }

    @Override
    public Producto guardar(Producto producto) {
        ValidarProducto(producto);
        if (producto.getEstado() == 0) {
            producto.setEstado(1);
        }
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCode(Long code) {
        return productoRepository.findById(code);
    }

    @Override
    public Producto ActualizarP(Long code, Producto producto) {
        if (!productoRepository.existsById(code)) {
            throw new RuntimeException("El Producto con el codigo" + code + "no se encuentra");
        }
        producto.setCodigoProducto(code);
        ValidarProducto(producto);
        return productoRepository.save(producto);
    }

    @Override
    public void eliminarP(Long code) {
        if (!productoRepository.existsById(code)) {
            throw new RuntimeException("El Producto no se encuentra con el codigo" + code);
        }
        productoRepository.deleteById(code);
    }

    @Override
    public boolean existCode(Long code) {
        return productoRepository.existsById(code);
    }

    private void ValidarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El Producto no puede ser null");
        }
        if (producto.getPrecio().signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser positivo");
        }
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El Stock no puede ser menor o a cero");
        }
    }
}
