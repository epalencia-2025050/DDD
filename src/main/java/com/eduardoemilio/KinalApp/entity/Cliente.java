package com.eduardoemilio.KinalApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @Column(name = "dpi_cliente")
    private String DPICliente;
    @Column(nullable = false)
    private String nombreCliente;
    @Column(nullable = false)
    private String apellidoCliente;
    @Column(nullable = false)
    private String direccion;
    @Column(nullable = false)
    private int estado;

    public Cliente() {
    }

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Venta> ventas;

    public Cliente(String DPICliente, String nombreCliente, String apellidoCliente, String direccion, int estado, List<Venta> ventas) {
        this.DPICliente = DPICliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.direccion = direccion;
        this.estado = estado;
        this.ventas = ventas;
    }

    public String getDPICliente() {
        return DPICliente;
    }

    public void setDPICliente(String DPICliente) {
        this.DPICliente = DPICliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }
}
