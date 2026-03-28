package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.Usuario;
import com.eduardoemilio.KinalApp.entity.Venta;
import com.eduardoemilio.KinalApp.service.IVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;

    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listarVenta(){
        List<Venta> ventas = ventaService.listarVenta();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/activos/{estado}")
    public ResponseEntity<List<Venta>> VentaEstado(@PathVariable int estado){
        List<Venta> ventas = ventaService.listarEstadoVenta(estado);
        return ResponseEntity.ok(ventaService.listarEstadoVenta(estado));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Venta> buscarPorCode(@PathVariable int code){
        return ventaService.buscarPorCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Venta venta){
        System.out.println("Venta recibida: " + venta);
        System.out.println("Cliente: " + venta.getCliente());
        System.out.println("Usuario: " + venta.getUsuario());
        try{
            Venta nuevaVenta = ventaService.guardar(venta);
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @DeleteMapping("/{code}")
       public ResponseEntity<Void> eliminar(@PathVariable int code){
        try{
            if(!ventaService.existCodeV(code)){
                return ResponseEntity.notFound().build();
            }
            ventaService.eliminarV(code);
            return ResponseEntity.noContent().build();
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> Actualizar(@PathVariable int code, @RequestBody Venta venta){
        try{
            if(!ventaService.existCodeV(code)){
                return ResponseEntity.notFound().build();
            }
            Venta ventaAc = ventaService.ActualizarV(code, venta);
            return ResponseEntity.ok(ventaAc);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

}
