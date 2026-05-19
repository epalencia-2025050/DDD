package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.Cliente;

import com.eduardoemilio.KinalApp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Anotacion que registra un Bean como Bean de Spring
//Que la clase contiene la logica del negocio
@Service
//Por defecto todos los metodos de enta clase seran
//Transaccionales
//Una transaccion es que puede o no ocurrir algo
@Transactional
public class clienteService implements IClienteService {
    /* Private: solo accesible dentro de la clase
       Cliente repository: Es el repositorio para acceder a BD
       Inyeccion de Depencias Spring nos da el repositorio

    */
    private final ClienteRepository clienteRepository;

    /*
     * Contructor: este se ejecuta al crear un objeto
     * Parametros: Spring pasa el repositorio automaticamente y a este se le conoce
     * como Inyeccion de dependencias
     * Asignamos el repositorio a nuestra variable de clase
     */

    public clienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    //Este metodo es el mismo que el de la linea 54 pero adaptado
    //retorna el estado del cliente con su informacion
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> clienteEstado(int estado) {
        return clienteRepository.findByEstado(estado);
    }

    /*
    * @override: Indica que estamos implementando  un metodo intefaz
    * */
    @Override
    /*
    readOnly = true: lo que hace es optimizar la consulta, no bloquea la BD
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
        /*
        *Llama al metodo finAll() del repositorio de Spring data
         */
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        /*
        * Metodo de guardar crea un Cliente
        * Aca es donde colocamos la logica del negocio antes de guardar
        * primero validamos el dato
        * */
        validarCliente(cliente);
        if(cliente.getEstado()==0){
            cliente.setEstado(1);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> bucarPorDPI(String dpi) {
        //Busca el Cliente por DPI
        return clienteRepository.findById(dpi);
        //Opcional nos evita el nullpointerException
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        //Actualiza un cliente existente
        if(!clienteRepository.existsById(dpi)){
            throw new RuntimeException("cliente no se encontro con DPI "+ dpi);
            //Si no existe, se lanza una excepcion (error controlado)
        }
        /*
        * 1. Asegurar que el DPI del objeto coincida con el del URL
        * 2. Por seguridad usamos el DPI de la URL y no el que viene de JSON
        * */
        cliente.setDPICliente(dpi);
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {
      //Eliminar un Cliente
        if(!clienteRepository.existsById(dpi)){
            throw new RuntimeException("cliente no se encontro con el DPI "+dpi);
        }
        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeDPI(String dpi) {
        //Verifica si existe Cliente
        return clienteRepository.existsById(dpi);
        //Retorna true o false
    }

    @Override
    public boolean existeDpi(String dpi) {
        return clienteRepository.existsById(dpi);
    }

    //Metodo privado(Solo pueden utilizarse dentro de la clase)
    private void validarCliente(Cliente cliente){
        /*
        * Validaciones del negocio: Este metodo se hara privado porque
        * es algo interno del servicio
        * */
        if(cliente.getDPICliente() == null || cliente.getDPICliente().trim().isEmpty()){
            //Si el DPI es null o esta vacio despues de quitar espacios
            //Lanza una excepcion con um mensaje
            throw new IllegalArgumentException("El DPI es un dato obligatorio");
        }

        if(cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es un dato obligtorio");
        }

        if(cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El apellido es un dato obligtorio");
        }

    }




}
