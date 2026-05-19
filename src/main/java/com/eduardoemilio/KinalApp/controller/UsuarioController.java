package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.Usuario;
import com.eduardoemilio.KinalApp.service.IUsuarioService;
import com.eduardoemilio.KinalApp.service.IVentaService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(IUsuarioService usuarioService, IVentaService ventaService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listar(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuario();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Listado de Usuarios");
        return "usuario/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("titulo", "Nuevo Usuario");
        return "usuario/formulario";
    }

    @GetMapping("/editar/{code}")
    public String editar(@PathVariable Long code, Model model, RedirectAttributes flash) {
        return usuarioService.buscarPorcode(code)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    model.addAttribute("titulo", "Editar Usuario");
                    return "usuario/formulario";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "Usuario no encontrado");
                    return "redirect:/usuarios";
                });
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash) {
        if (usuario.getEmail() != null && usuarioService.existeEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.usuario", "Ya existe un usuario con ese email");
        }
        if (usuario.getUsername() != null && usuarioService.existeUsername(usuario.getUsername())) {
            result.rejectValue("username", "error.usuario", "Ya existe un usuario con ese nombre");
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Usuario");
            return "usuario/formulario";
        }

        try {
            usuarioService.guardarU(usuario);
            flash.addFlashAttribute("mensaje", "Usuario guardado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("mensaje", e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/actualizar/{code}")
    public String actualizar(@PathVariable Long code,
                             @Valid @ModelAttribute("usuario") Usuario usuario,
                             BindingResult result,
                             Model model,
                             RedirectAttributes flash) {

        Usuario existing = usuarioService.buscarPorcode(code).orElse(null);
        if (existing == null) {
            flash.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/usuarios";
        }

        if (usuario.getEmail() != null && !usuario.getEmail().equals(existing.getEmail())
                && usuarioService.existeEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.usuario", "Ya existe un usuario con ese email");
        }
        if (usuario.getUsername() != null && !usuario.getUsername().equals(existing.getUsername())
                && usuarioService.existeUsername(usuario.getUsername())) {
            result.rejectValue("username", "error.usuario", "Ya existe un usuario con ese nombre");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            usuario.setPassword(existing.getPassword());
        } else {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        usuario.setCodigoUsuario(code);

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Usuario");
            return "usuario/formulario";
        }

        try {
            usuarioService.ActualizarU(code, usuario);
            flash.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("mensaje", e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{code}")
    public String eliminar(@PathVariable Long code, RedirectAttributes flash) {
        try {
            usuarioService.eliminarU(code);
            flash.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            flash.addFlashAttribute("mensaje", "Error al eliminar");
            flash.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/activos/{estado}")
    public String usuarioEstado(@PathVariable int estado, Model model) {
        List<Usuario> usuarios = usuarioService.UsuarioEstado(estado);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", estado == 1 ? "Usuarios Activos" : "Usuarios Inactivos");
        model.addAttribute("filtroEstado", estado);
        return "usuario/lista";
    }
}