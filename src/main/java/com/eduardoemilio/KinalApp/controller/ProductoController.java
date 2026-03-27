package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.Producto;
import com.eduardoemilio.KinalApp.service.IProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Productos")
public class ProductoController {

   private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProducto(){
        List<Producto> productos = productoService.listarProducto();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/activos/{estado}")
    public ResponseEntity<List<Producto>> productoEstado(@PathVariable int estado){
        List<Producto> productos = productoService.ProductoEstado(estado);
        return ResponseEntity.ok(productoService.ProductoEstado(estado));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Producto> buscarPorCode(@PathVariable Long code){
        return productoService.buscarPorCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Producto producto){
        try{
            Producto nuevoProducto = productoService.guardar(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> eliminar(@PathVariable Long code){
        try{
            if(!productoService.existCode(code)){
                return ResponseEntity.notFound().build();
            }
            productoService.eliminarP(code);
            return ResponseEntity.noContent().build();
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> actualizar(@PathVariable Long code, @RequestBody Producto producto){
        try{
            if(!productoService.existCode(code)){
                return ResponseEntity.notFound().build();
            }
            Producto nuevoProducto = productoService.ActualizarP(code, producto);
            return ResponseEntity.ok(nuevoProducto);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }

    }
}
