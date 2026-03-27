package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.DetalleVenta;
import com.eduardoemilio.KinalApp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class detalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    public detalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarDVenta() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        ValidarDetalleVenta(detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCode(Long code) {
        return detalleVentaRepository.findById(code);
    }

    @Override
    public DetalleVenta actualizarD(Long code, DetalleVenta detalleVenta) {
        if(!detalleVentaRepository.existsById(code)){
            throw new RuntimeException("Detalle de la venta no encotrado con el codigo " + code);
        }
        detalleVenta.setCodigoDetalleVenta(code);
        ValidarDetalleVenta(detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminarD(Long code) {
      if(!detalleVentaRepository.existsById(code)){
          throw new RuntimeException("Detalle de la venta no encontrado" + code);
      }
      detalleVentaRepository.deleteById(code);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCode(Long code) {
        return detalleVentaRepository.existsById(code);
    }

    public void ValidarDetalleVenta(DetalleVenta detalleVenta){
        if (detalleVenta == null){
            throw new IllegalArgumentException("El detalle de venta no puede ser null");
        }
        if (detalleVenta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (detalleVenta.getPrecioUnitario() == null || detalleVenta.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }
        if (detalleVenta.getSubtotal() == null || detalleVenta.getSubtotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El subtotal debe ser mayor a cero");
        }

    }

}
