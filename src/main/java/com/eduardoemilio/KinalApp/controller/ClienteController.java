package com.eduardoemilio.KinalApp.controller;

import com.eduardoemilio.KinalApp.entity.Cliente;
import com.eduardoemilio.KinalApp.repository.ClienteRepository;
import com.eduardoemilio.KinalApp.service.IClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RestController = @Controller = @ResponseBody
@RequestMapping("/clientes")
//Todas las rutas en este controlador deben empezar con /clientes
public class ClienteController {

    //Inyectamos el SERVICIO y no el repositorio
    //El controlador solo debe de tener conexion con el servicio
    private final IClienteService clienteService;

    //Como buena practica la Inyecion de dependencias
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    //Responde a peticiones get
    @GetMapping
    //ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity <List<Cliente>> listar(){
        List<Cliente> clientes = clienteService.listarTodos();
        //Deniega el servicio
        return ResponseEntity.ok(clientes);
    }

    //(dpi) es una variable de ruta(valor a buscar)
    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDPI(@PathVariable String dpi){
        //@PathVarible toma el valor de la URL y lo asigna al dpi
        return clienteService.bucarPorDPI(dpi)
                //Si optional tiene el valor, devuelve 200 ok con el cliente
        .map(ResponseEntity::ok)
                //Si optional esta vacio, devulve 404 no found
        .orElse(ResponseEntity.notFound().build());
    }


    //Post crea un nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente){
        //@RequestBody Toma el Json del cuerpo y lo combierte a un objeto de tipo CLiente
        //<?> significa "Tipo generico" puede ser un Cliente o un String
        try{
            Cliente nuevoCliente = clienteService.guardar(cliente);
            //Intentamos guardar el cliente para puede lanzar una exception
            //de IllegalArgumentException
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        }catch(IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Delete elimina a un cliente
    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable String dpi){
        //ResponseEntity<Void>: devuelve cuerpo de la respuesta
        try{
            if(!clienteService.existeDPI(dpi)){
                return ResponseEntity.notFound().build();
                //404 si no existe
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
            //204 no content(Se ejecuta correctamente pero no deulve cuerpo)
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }
    }

    //EL metodo es el mismo solo que adaptado del listar en la linea 31
    //Este metodo es para ver la actividad del usuario si esta activo o inactivo
    //Listamos los clientes con su respectivo INT de estado(1 o 0)
    @GetMapping("/activos/{estado}")
    public ResponseEntity<List<Cliente>> clienteEstado(@PathVariable int estado){
        List<Cliente> clientes = clienteService.clienteEstado(estado);
        return ResponseEntity.ok(clienteService.clienteEstado(estado));
        // 200 ok del metodo clienteEstado
    }

    //Actualizar cliente a traves del DPI
    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable String dpi, @RequestBody Cliente cliente){
        try{
            if(!clienteService.existeDPI(dpi)){
                //Verificar si existe antes de poder actualizar
                //404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
            //Actualiza este cliente pero puede lanzar una exception
            Cliente clienteActualizar = clienteService.actualizar(dpi, cliente);
            return ResponseEntity.ok(clienteActualizar);
            //200 ok con cliente actualizar s
        }catch(IllegalArgumentException e){
            //Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());//getMessage es para capturar el mensaje
        }catch(RuntimeException e){
            //Posiblemente cualquier otro error, por ejemplo: cliente no encotrado, etc.
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }

    }

}

