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
    private final IVentaService ventaService;
    private final IProductoService productoService;


    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository,
                               VentaRepository ventaRepository,
                               ProductoRepository productoRepository, IVentaService ventaService, IProductoService productoService) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarDVenta() {
        return detalleVentaRepository.findAll();
    }

    @Override
    @Transactional
    public DetalleVenta guardar(DetalleVenta detalle) {
        if (detalle.getVenta() == null) {
            throw new IllegalArgumentException("Debe seleccionar una venta");
        }
        if (detalle.getProducto() == null) {
            throw new IllegalArgumentException("Debe seleccionar un producto");
        }
        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }

        Producto producto = detalle.getProducto();
        if (producto.getStock() < detalle.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + producto.getStock());
        }
        producto.setStock(producto.getStock() - detalle.getCantidad());
        productoService.actualizar(producto);

        if (detalle.getSubtotal() == null || detalle.getSubtotal().compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal subtotal = BigDecimal.valueOf(detalle.getCantidad())
                    .multiply(detalle.getPrecioUnitario());
            detalle.setSubtotal(subtotal);
        }

        DetalleVenta detalleGuardado = detalleVentaRepository.save(detalle);

        Venta venta = detalle.getVenta();
        BigDecimal nuevoTotal = calcularTotalVenta(venta.getCodigoVenta());
        venta.setTotal(nuevoTotal);
        ventaService.actualizar(venta);

        return detalleGuardado;
    }

    private BigDecimal calcularTotalVenta(Long codigoVenta) {
        List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_CodigoVenta(codigoVenta);
        return detalles.stream()
                .map(DetalleVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
        Venta managedVenta = ventaRepository.findById(venta.getCodigoVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + venta.getCodigoVenta()));

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
    }
}
