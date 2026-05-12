package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.Venta;
import com.eduardoemilio.KinalApp.service.IVentaService;
import com.eduardoemilio.KinalApp.service.IClienteService;
import com.eduardoemilio.KinalApp.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    public VentaController(IVentaService ventaService, IClienteService clienteService, IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarVenta(Model model) {
        List<Venta> ventas = ventaService.listarVenta();
        model.addAttribute("ventas", ventas);
        model.addAttribute("titulo", "Listado de Ventas");
        return "venta/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Venta venta = new Venta();
        venta.setFechaVenta(LocalDate.now());
        venta.setEstado(1);
        model.addAttribute("venta", venta);
        model.addAttribute("titulo", "Nueva Venta");
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarUsuario());
        return "venta/formulario";
    }

    @GetMapping("/editar/{code}")
    public String editar(@PathVariable int code, Model model, RedirectAttributes flash) {
        return ventaService.buscarPorCode(code)
                .map(venta -> {
                    model.addAttribute("venta", venta);
                    model.addAttribute("titulo", "Editar Venta");
                    model.addAttribute("clientes", clienteService.listarTodos());
                    model.addAttribute("usuarios", usuarioService.listarUsuario());
                    return "venta/formulario";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "Venta no encontrada");
                    return "redirect:/ventas";
                });
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta,
                          @RequestParam(required = false) String clienteDpi,
                          @RequestParam(required = false) Long usuarioCode,
                          RedirectAttributes flash) {
        try {
            if (clienteDpi != null && !clienteDpi.isEmpty()) {
                clienteService.bucarPorDPI(clienteDpi).ifPresent(venta::setCliente);
            }
            if (usuarioCode != null) {
                usuarioService.buscarPorcode(usuarioCode).ifPresent(venta::setUsuario);
            }
            ventaService.guardar(venta);
            flash.addFlashAttribute("mensaje", "Venta guardada exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("mensaje", e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/ventas";
    }

    @PostMapping("/actualizar/{code}")
    public String actualizar(@PathVariable int code, @ModelAttribute Venta venta, RedirectAttributes flash) {
        try {
            ventaService.ActualizarV(code, venta);
            flash.addFlashAttribute("mensaje", "Venta actualizada exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("mensaje", e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/ventas";
    }

    @GetMapping("/eliminar/{code}")
    public String eliminar(@PathVariable int code, RedirectAttributes flash) {
        try {
            ventaService.eliminarV(code);
            flash.addFlashAttribute("mensaje", "Venta eliminada exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            flash.addFlashAttribute("mensaje", "Error al eliminar");
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/ventas";
    }

    @GetMapping("/ver/{code}")
    public String ver(@PathVariable int code, Model model, RedirectAttributes flash) {
        return ventaService.buscarPorCode(code)
                .map(venta -> {
                    model.addAttribute("venta", venta);
                    model.addAttribute("titulo", "Detalle de Venta #" + code);
                    return "venta/detalle";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "Venta no encontrada");
                    return "redirect:/ventas";
                });
    }

    @GetMapping("/activos/{estado}")
    public String ventaEstado(@PathVariable int estado, Model model) {
        List<Venta> ventas = ventaService.listarEstadoVenta(estado);
        model.addAttribute("ventas", ventas);
        model.addAttribute("titulo", estado == 1 ? "Ventas Activas" : "Ventas Inactivas");
        model.addAttribute("filtroEstado", estado);
        return "venta/lista";
    }
}