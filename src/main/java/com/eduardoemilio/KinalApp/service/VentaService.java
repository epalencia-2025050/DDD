package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.Cliente;
import com.eduardoemilio.KinalApp.entity.Usuario;
import com.eduardoemilio.KinalApp.entity.Venta;
import com.eduardoemilio.KinalApp.repository.ClienteRepository;
import com.eduardoemilio.KinalApp.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    public VentaService(VentaRepository ventaRepository, UsuarioRepository usuarioRepository, ClienteRepository clienteRepository) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
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
        // Validar que el cliente y usuario no sean nulos
        if (venta.getCliente() == null || venta.getCliente().getDPICliente() == null) {
            throw new IllegalArgumentException("Debe proporcionar un cliente con DPI válido");
        }
        if (venta.getUsuario() == null || venta.getUsuario().getCodigoUsuario() == null) {
            throw new IllegalArgumentException("Debe proporcionar un usuario con código válido");
        }

        Cliente cliente = clienteRepository.findById(venta.getCliente().getDPICliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con DPI: " + venta.getCliente().getDPICliente()));

        Usuario usuario = usuarioRepository.findById(venta.getUsuario().getCodigoUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con código: " + venta.getUsuario().getCodigoUsuario()));

        venta.setCliente(cliente);
        venta.setUsuario(usuario);

        if (venta.getTotal() == null) {
            venta.setTotal(BigDecimal.ZERO);
        }
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> buscarPorCode(int code) {
        return ventaRepository.findById((long) code);
    }

    @Override
    public Venta ActualizarV(int code, Venta venta) {
        if(!ventaRepository.existsById((long) code)){
            throw new RuntimeException("Venta no encontrada con code " + code);
        }
        venta.setCodigoVenta((long) code);
        validarVenta(venta);
        return ventaRepository.save(venta) ;
    }

    @Override
    public void eliminarV(int code) {
        if(!ventaRepository.existsById((long) code)){
            throw new RuntimeException("No se encontro por code");
        }
        ventaRepository.deleteById((long) code);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existCodeV(int code) {
        return ventaRepository.existsById((long) code);
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
