package com.eduardoemilio.KinalApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_producto")
    private Long codigoProducto;
    @Column(nullable = false)
    private String nombreProducto;
    @Column(precision = 10, scale = 2,nullable = false)
    private BigDecimal precio;
    @Column(nullable = false)
    private int stock;
    @Column(nullable = false)
    private int estado;

    public Producto() {
    }

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DetalleVenta> detalleVentas;

    public Producto(Long codigoProducto, String nombreProducto, BigDecimal precio, int stock, int estado, List<DetalleVenta> detalleVentas) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
        this.detalleVentas = detalleVentas;
    }

    public Long getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Long codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public List<DetalleVenta> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<DetalleVenta> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }
}
