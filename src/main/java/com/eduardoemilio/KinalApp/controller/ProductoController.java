package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.Producto;
import com.eduardoemilio.KinalApp.service.IProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listarProducto(Model model) {
        List<Producto> productos = productoService.listarProducto();
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de Productos");
        return "producto/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "Nuevo Producto");
        return "producto/formulario";
    }

    @GetMapping("/editar/{code}")
    public String editar(@PathVariable Long code, Model model, RedirectAttributes flash) {
        return productoService.buscarPorCode(code)
                .map(producto -> {
                    model.addAttribute("producto", producto);
                    model.addAttribute("titulo", "Editar Producto");
                    return "producto/formulario";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "Producto no encontrado");
                    return "redirect:/productos";
                });
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Producto");
            return "producto/formulario";
        }

        try {
            productoService.guardar(producto);
            flash.addFlashAttribute("mensaje", "Producto guardado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("mensaje", e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/productos";
    }

    @PostMapping("/actualizar/{code}")
    public String actualizar(@PathVariable Long code,
                             @Valid @ModelAttribute("producto") Producto producto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Producto");
            return "producto/formulario";
        }

        try {
            productoService.ActualizarP(code, producto);
            flash.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("mensaje", e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{code}")
    public String eliminar(@PathVariable Long code, RedirectAttributes flash) {
        try {
            productoService.eliminarP(code);
            flash.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            flash.addFlashAttribute("mensaje", "Error al eliminar");
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/productos";
    }

    @GetMapping("/activos/{estado}")
    public String productoEstado(@PathVariable int estado, Model model) {
        List<Producto> productos = productoService.ProductoEstado(estado);
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", estado == 1 ? "Productos Activos" : "Productos Inactivos");
        model.addAttribute("filtroEstado", estado);
        return "producto/lista";
    }
}