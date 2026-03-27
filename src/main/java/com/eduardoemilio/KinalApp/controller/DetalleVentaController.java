package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.DetalleVenta;
import com.eduardoemilio.KinalApp.service.IDetalleVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/DetalleVentas")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @PostMapping("/{code}")
    public ResponseEntity<?> actualizarV(@PathVariable Long code, @RequestBody DetalleVenta detalleVenta){
        try{
            if(!detalleVentaService.existeCode(code)){
                return ResponseEntity.notFound().build();
            }
            DetalleVenta acDetalleVenta = detalleVentaService.actualizarD(code, detalleVenta);
            return ResponseEntity.ok(acDetalleVenta);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> eliminar(@PathVariable Long code){
        try{
            if(!detalleVentaService.existeCode(code)){
                return ResponseEntity.notFound().build();
            }
            detalleVentaService.eliminarD(code);
            return ResponseEntity.noContent().build();
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> guardarD(@RequestBody DetalleVenta detalleVenta){
        try{
            DetalleVenta nuevoDetalleVenta = detalleVentaService.guardar(detalleVenta);
            return new ResponseEntity<>(nuevoDetalleVenta, HttpStatus.CREATED);
        }catch(IllegalArgumentException e){

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<DetalleVenta> buscarPorCode(@PathVariable Long code){
        return detalleVentaService.buscarPorCode(code)
                .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listarV(){
        List<DetalleVenta> detalleVentas = detalleVentaService.listarDVenta();
        return ResponseEntity.ok(detalleVentas);
    }


}
