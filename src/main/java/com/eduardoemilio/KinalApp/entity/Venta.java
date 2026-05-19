package com.eduardoemilio.KinalApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_venta")
    private Long codigoVenta;

    @NotNull(message = "La fecha de venta es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(nullable = false)
    private LocalDate fechaVenta;

    @NotNull(message = "El total es obligatorio")
    @PositiveOrZero(message = "El total no puede ser negativo")
    @DecimalMin(value = "0.00", message = "El total debe ser mayor o igual a 0")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Min(value = 0, message = "Estado inválido")
    @Max(value = 1, message = "Estado inválido")
    @Column(nullable = false)
    private int estado;

    public Venta() {
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull(message = "Debe seleccionar un cliente")
    @JoinColumn(name = "clientes_dpi_cliente", referencedColumnName = "dpi_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull(message = "Debe seleccionar un usuario")
    @JoinColumn(name = "usuarios_codigo_usuario", referencedColumnName = "codigo_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta(Long codigoVenta, LocalDate fechaVenta, BigDecimal total, int estado, Cliente cliente, Usuario usuario, List<DetalleVenta> detalles) {
        this.codigoVenta = codigoVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = estado;
        this.cliente = cliente;
        this.usuario = usuario;
        this.detalles = detalles;
    }

    public Long getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(Long codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}
