package com.eduardoemilio.KinalApp.service;
import com.eduardoemilio.KinalApp.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listarUsuario();

    Usuario guardarU(Usuario usuario);

    Usuario ActualizarU(Long code, Usuario usuario);

    void eliminarU(Long code);

    boolean existcode(Long code);

    Optional<Usuario> buscarPorcode(Long code);

    List<Usuario> UsuarioEstado(int estado);
    boolean existeEmail(String email);
    boolean existeUsername(String username);
}
