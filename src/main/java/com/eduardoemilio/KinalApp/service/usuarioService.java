package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.Usuario;
import com.eduardoemilio.KinalApp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class usuarioService implements IUsuarioService{

    private final UsuarioRepository usuarioRepository;

    public usuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardarU(Usuario usuario) {
        ValiidarUsuario(usuario);
        if(usuario.getEstado() == 0){
            usuario.setEstado(1);
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario ActualizarU(Long code, Usuario usuario) {

        if(!usuarioRepository.existsById(code)){
            throw new RuntimeException("Usuario no se encuentra con el codigoUsuario " + code);
        }
        usuario.setCodigoUsuario(code);
        ValiidarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarU(Long code) {

        if(!usuarioRepository.existsById(code)){
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

    private void ValiidarUsuario(Usuario usuario){
        if(usuario == null){
            throw new IllegalArgumentException("El usuario no puede ser null");
        }

        if(usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()){
            throw new IllegalArgumentException("El username es obligatorio");
        }

        if(usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()){
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if(usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()){
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if(usuario.getRol() == null || usuario.getRol().trim().isEmpty()){
            throw new IllegalArgumentException("El rol es obligatorio");
        }
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}