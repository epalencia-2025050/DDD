package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.Usuario;
import com.eduardoemilio.KinalApp.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/Usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> usuarios = usuarioService.listarUsuario();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Usuario> buscarPorcode(@PathVariable Long code){
        return usuarioService.buscarPorcode(code)
         .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario){
        try{
            Usuario nuevoUsuario = usuarioService.guardarU(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        }catch(IllegalArgumentException e){

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> eliminar(@PathVariable Long code){
        try{
            if(!usuarioService.existcode(code)){
                return ResponseEntity.notFound().build();
            }
            usuarioService.eliminarU(code);
            return ResponseEntity.noContent().build();
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activos/{estado}")
    public ResponseEntity<List<Usuario>> usuarioEstado(@PathVariable int estado){
        List<Usuario> usuarios = usuarioService.UsuarioEstado(estado);
        return ResponseEntity.ok(usuarioService.UsuarioEstado(estado));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> Actualizar(@PathVariable Long code, @RequestBody Usuario usuario){
        try{
            if(!usuarioService.existcode(code)){
                return ResponseEntity.notFound().build();
            }
            Usuario usuarioActualizar = usuarioService.ActualizarU(code, usuario);
            return ResponseEntity.ok(usuarioActualizar);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }

    }

}
