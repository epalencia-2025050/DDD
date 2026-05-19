package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.Usuario;
import com.eduardoemilio.KinalApp.service.usuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    private final usuarioService usuarioService;

    public RegistroController(usuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@RequestParam String username,
                            @RequestParam String email,
                            @RequestParam String password,
                            RedirectAttributes flash) {
        try {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword(password);
            usuarioService.guardarU(u);
            flash.addFlashAttribute("mensaje", "Registro exitoso. Ahora inicia sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }
}