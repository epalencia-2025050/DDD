package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.Venta;
import com.eduardoemilio.KinalApp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService implements IVentaService{

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarVenta() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarEstadoVenta(int estado) {

        return ventaRepository.findByEstado(estado);
    }

    @Override
    public Venta guardar(Venta venta) {
        validarVenta(venta);
        if(venta.getEstado()==0)
            venta.setEstado(1);
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> buscarPorCode(Long code) {
        return ventaRepository.findById(code);
    }

    @Override
    public Venta ActualizarV(Long code, Venta venta) {
        if(!ventaRepository.existsById(code)){
            throw new RuntimeException("Venta no encontrada con code " + code);
        }
        venta.setCodigoVenta(code);
        validarVenta(venta);
        return ventaRepository.save(venta) ;
    }

    @Override
    public void eliminarV(Long code) {
        if(!ventaRepository.existsById(code)){
            throw new RuntimeException("No se encontro por code");
        }
        ventaRepository.deleteById(code);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existCodeV(Long code) {
        return ventaRepository.existsById(code);
    }

    private void validarVenta(Venta venta){
        if (venta == null) {
            throw new IllegalArgumentException("Venta no puede ser null");
        }
        if (venta.getFechaVenta() == null) {
            throw new IllegalArgumentException("La fecha no puede ser null");
        }

        if (venta.getTotal() == null || venta.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El total debe ser mayor a cero");
        }
    }
}
