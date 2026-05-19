package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.Cliente;
import com.eduardoemilio.KinalApp.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Cliente> clientes = clienteService.listarTodos();
        model.addAttribute("clientes", clientes);
        model.addAttribute("titulo", "Listado de Clientes");
        return "cliente/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "cliente/formulario";
    }

    @GetMapping("/editar/{dpi}")
    public String editar(@PathVariable String dpi, Model model, RedirectAttributes flash) {
        return clienteService.bucarPorDPI(dpi)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    model.addAttribute("titulo", "Editar Cliente");
                    return "cliente/formulario";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "Cliente no encontrado");
                    return "redirect:/clientes";
                });
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash) {
        if (cliente.getDPICliente() != null && clienteService.existeDpi(cliente.getDPICliente())) {
            result.rejectValue("DPICliente", "error.cliente", "Ya existe un cliente con ese DPI");
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Cliente");
            return "cliente/formulario";
        }

        try {
            clienteService.guardar(cliente);
            flash.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("mensaje", e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/clientes";
    }

    @PostMapping("/actualizar/{dpi}")
    public String actualizar(@PathVariable String dpi,
                             @Valid @ModelAttribute("cliente") Cliente cliente,
                             BindingResult result,
                             Model model,
                             RedirectAttributes flash) {
        if (cliente.getDPICliente() != null && !cliente.getDPICliente().equals(dpi)
                && clienteService.existeDpi(cliente.getDPICliente())) {
            result.rejectValue("DPICliente", "error.cliente", "Ya existe un cliente con ese DPI");
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Cliente");
            return "cliente/formulario";
        }

        try {
            clienteService.actualizar(dpi, cliente);
            flash.addFlashAttribute("mensaje", "Cliente actualizado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("mensaje", e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi, RedirectAttributes flash) {
        try {
            clienteService.eliminar(dpi);
            flash.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            flash.addFlashAttribute("mensaje", "Error al eliminar");
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/clientes";
    }

    @GetMapping("/activos/{estado}")
    public String clienteEstado(@PathVariable int estado, Model model) {
        List<Cliente> clientes = clienteService.clienteEstado(estado);
        model.addAttribute("clientes", clientes);
        model.addAttribute("titulo", estado == 1 ? "Clientes Activos" : "Clientes Inactivos");
        model.addAttribute("filtroEstado", estado);
        return "cliente/lista";
    }
}