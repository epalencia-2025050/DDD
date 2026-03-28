package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.DetalleVenta;
import com.eduardoemilio.KinalApp.entity.Producto;
import com.eduardoemilio.KinalApp.entity.Venta;
import com.eduardoemilio.KinalApp.repository.DetalleVentaRepository;
import com.eduardoemilio.KinalApp.repository.ProductoRepository;
import com.eduardoemilio.KinalApp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository,
                               VentaRepository ventaRepository,
                               ProductoRepository productoRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarDVenta() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        validarDetalleVenta(detalleVenta);
        Venta venta = ventaRepository.findById(detalleVenta.getVenta().getCodigoVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + detalleVenta.getVenta().getCodigoVenta()));
        Producto producto = productoRepository.findById(detalleVenta.getProducto().getCodigoProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalleVenta.getProducto().getCodigoProducto()));

        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);
        calcularSubtotal(detalleVenta);

        DetalleVenta saved = detalleVentaRepository.save(detalleVenta);
        actualizarTotalVenta(saved.getVenta());

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCode(Long code) {
        return detalleVentaRepository.findById(code);
    }

    @Override
    public DetalleVenta actualizarD(Long code, DetalleVenta detalleVenta) {
        if (!detalleVentaRepository.existsById(code)) {
            throw new RuntimeException("Detalle de venta no encontrado con el código " + code);
        }
        detalleVenta.setCodigoDetalleVenta(code);
        validarDetalleVenta(detalleVenta);

        calcularSubtotal(detalleVenta);
        DetalleVenta updated = detalleVentaRepository.save(detalleVenta);

        actualizarTotalVenta(updated.getVenta());

        return updated;
    }

    @Override
    public void eliminarD(Long code) {
        DetalleVenta detalle = detalleVentaRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado: " + code));
        Venta venta = detalle.getVenta();
        detalleVentaRepository.delete(detalle);
        actualizarTotalVenta(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCode(Long code) {
        return detalleVentaRepository.existsById(code);
    }

    private void calcularSubtotal(DetalleVenta detalle) {
        BigDecimal cantidad = BigDecimal.valueOf(detalle.getCantidad());
        detalle.setSubtotal(cantidad.multiply(detalle.getPrecioUnitario()));
    }

    private void actualizarTotalVenta(Venta venta) {
        // Recargar la venta desde la base de datos para tener los detalles actualizados
        Venta managedVenta = ventaRepository.findById(venta.getCodigoVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + venta.getCodigoVenta()));

        // Sumar los subtotales de los detalles
        BigDecimal total = managedVenta.getDetalles().stream()
                .map(DetalleVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        managedVenta.setTotal(total);
        ventaRepository.save(managedVenta);
    }

    private void validarDetalleVenta(DetalleVenta detalleVenta) {
        if (detalleVenta == null) {
            throw new IllegalArgumentException("El detalle de venta no puede ser null");
        }
        if (detalleVenta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (detalleVenta.getPrecioUnitario() == null ||
                detalleVenta.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }
        // No validamos subtotal porque se calcula automáticamente
    }
}
