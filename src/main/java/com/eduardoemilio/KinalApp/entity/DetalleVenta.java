package com.eduardoemilio.KinalApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "detalle_venta") 
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_detalle_venta")
    private Long codigoDetalleVenta;
    @Column(nullable = false)
    private int cantidad;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    public DetalleVenta() {
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ventas_codigo_venta", referencedColumnName = "codigo_venta", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_producto", referencedColumnName = "codigo_producto", nullable = false)
    private Producto producto;

    public DetalleVenta(Long codigoDetalleVenta, int cantidad, BigDecimal precioUnitario, BigDecimal subtotal, Venta venta, Producto producto) {
        this.codigoDetalleVenta = codigoDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.venta = venta;
        this.producto = producto;
    }

    public Long getCodigoDetalleVenta() {
        return codigoDetalleVenta;
    }

    public void setCodigoDetalleVenta(Long codigoDetalleVenta) {
        this.codigoDetalleVenta = codigoDetalleVenta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
