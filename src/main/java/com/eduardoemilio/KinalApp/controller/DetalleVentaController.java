package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.DetalleVenta;
import com.eduardoemilio.KinalApp.service.IDetalleVentaService;
import com.eduardoemilio.KinalApp.service.IVentaService;
import com.eduardoemilio.KinalApp.service.IProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/detallesVentas")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;
    private final IVentaService ventaService;
    private final IProductoService productoService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService,
                                  IVentaService ventaService,
                                  IProductoService productoService) {
        this.detalleVentaService = detalleVentaService;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        List<DetalleVenta> detallesventas = detalleVentaService.listarDVenta();
        model.addAttribute("detalles", detallesventas);
        model.addAttribute("titulo", "Listado de Detalles de Venta");
        return "detalle/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(BigDecimal.ZERO);
        detalle.setSubtotal(BigDecimal.ZERO);
        model.addAttribute("detalle", detalle);
        model.addAttribute("titulo", "Nuevo Detalle de Venta");
        model.addAttribute("ventas", ventaService.listarVenta());
        model.addAttribute("productos", productoService.listarProducto());
        return "detalle/formulario";
    }

    @GetMapping("/editar/{code}")
    public String editar(@PathVariable Long code, Model model, RedirectAttributes flash) {
        return detalleVentaService.buscarPorCode(code)
                .map(detalle -> {
                    model.addAttribute("detalle", detalle);
                    model.addAttribute("titulo", "Editar Detalle de Venta");
                    model.addAttribute("ventas", ventaService.listarVenta());
                    model.addAttribute("productos", productoService.listarProducto());
                    return "detalle/formulario";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "Detalle no encontrado");
                    return "redirect:/detallesVentas";
                });
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute DetalleVenta detalleVenta,
                          @RequestParam(required = false) Long ventaId,
                          @RequestParam(required = false) Long productoId,
                          RedirectAttributes flash) {
        try {
            if (ventaId != null) {
                ventaService.buscarPorCode(ventaId.intValue()).ifPresent(detalleVenta::setVenta);
            }
            if (productoId != null) {
                productoService.buscarPorCode(productoId).ifPresent(detalleVenta::setProducto);
            }
            // Calcular subtotal
            if (detalleVenta.getCantidad() > 0 && detalleVenta.getPrecioUnitario() != null) {
                BigDecimal cantidadBD = BigDecimal.valueOf(detalleVenta.getCantidad());
                detalleVenta.setSubtotal(detalleVenta.getPrecioUnitario().multiply(cantidadBD));
            }
            detalleVentaService.guardar(detalleVenta);
            flash.addFlashAttribute("mensaje", "Detalle guardado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("mensaje", e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/detallesVentas";
    }

    @PostMapping("/actualizar/{code}")
    public String actualizar(@PathVariable Long code, @ModelAttribute DetalleVenta detalleVenta, RedirectAttributes flash) {
        try {
            // Calcular subtotal
            if (detalleVenta.getCantidad() > 0 && detalleVenta.getPrecioUnitario() != null) {
                BigDecimal cantidadBD = BigDecimal.valueOf(detalleVenta.getCantidad());
                detalleVenta.setSubtotal(detalleVenta.getPrecioUnitario().multiply(cantidadBD));
            }
            detalleVentaService.actualizarD(code, detalleVenta);
            flash.addFlashAttribute("mensaje", "Detalle actualizado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("mensaje", e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/detallesVentas";
    }

    @GetMapping("/eliminar/{code}")
    public String eliminar(@PathVariable Long code, RedirectAttributes flash) {
        try {
            detalleVentaService.eliminarD(code);
            flash.addFlashAttribute("mensaje", "Detalle eliminado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            flash.addFlashAttribute("mensaje", "Error al eliminar");
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/detallesVentas";
    }

    @GetMapping("/por-venta/{ventaId}")
    public String listarPorVenta(@PathVariable int ventaId, Model model) {
        List<DetalleVenta> todos = detalleVentaService.listarDVenta();
        List<DetalleVenta> filtrados = todos.stream()
                .filter(d -> d.getVenta() != null && d.getVenta().getCodigoVenta().equals(Long.valueOf(ventaId)))
                .toList();
        model.addAttribute("detalles", filtrados);
        model.addAttribute("titulo", "Detalles de Venta #" + ventaId);
        return "detalle/lista";
    }
}