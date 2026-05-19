package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.Usuario;
import com.eduardoemilio.KinalApp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class usuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public usuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario guardarU(Usuario usuario) {
        long totalUsuarios = usuarioRepository.count();
        if (totalUsuarios == 0) {
            usuario.setRol("ADMIN");
        } else {
            usuario.setRol("USER");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEstado(1);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario ActualizarU(Long code, Usuario usuario) {
        Usuario existing = usuarioRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existing.setUsername(usuario.getUsername());
        existing.setEmail(usuario.getEmail());
        existing.setRol(usuario.getRol());
        existing.setEstado(usuario.getEstado());

        if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
            if (!usuario.getPassword().startsWith("$2a$")) {
                existing.setPassword(passwordEncoder.encode(usuario.getPassword()));
            } else {
                existing.setPassword(usuario.getPassword());
            }
        }

        return usuarioRepository.save(existing);
    }

    @Override
    public void eliminarU(Long code) {
        if (!usuarioRepository.existsById(code)) {
            throw new RuntimeException("Usuario no encontrado con el code " + code);
        }
        usuarioRepository.deleteById(code);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existcode(Long code) {
        return usuarioRepository.existsById(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorcode(Long code) {
        return usuarioRepository.findById(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> UsuarioEstado(int estado) {
        return usuarioRepository.findByEstado(estado);
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existeUsername(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }
    }
}