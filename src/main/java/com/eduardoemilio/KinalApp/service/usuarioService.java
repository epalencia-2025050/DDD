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
        if (!usuarioRepository.existsById(code)) {
            throw new RuntimeException("Usuario no se encuentra con el codigoUsuario " + code);
        }
        Usuario existing = usuarioRepository.findById(code).get();
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            usuario.setPassword(existing.getPassword());
        } else if (!usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        usuario.setCodigoUsuario(code);
        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
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
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
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