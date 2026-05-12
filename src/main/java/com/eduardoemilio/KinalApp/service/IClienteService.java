package com.eduardoemilio.KinalApp.service;

import com.eduardoemilio.KinalApp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    //Interfaz: Es un contrato que dice que metodos debe tener
    //cualquier servicio de Cliente, No tiene
    //implementacion, solo la definicion de los metodos

    //Metodo que devulve una lista de todos los Clientes
    List<Cliente> listarTodos();
    //List<Cliente> los que hace es devolver una lista
    //de objetos de la entidad Clientes

    //Metodo que guarda un Cliente en la base de datos
    Cliente guardar(Cliente cliente);
    //Parametros - Recibe un ibjeto Cliente con los datos a guardar

    //Optional - Contenedor que puede o no tener un valor
    //evita el error de NUllPointerException
    Optional<Cliente> bucarPorDPI(String dpi);

    //Metodo que actualiza un Cliente
    Cliente actualizar(String dpi, Cliente cliente);
    //parametros - dpi: DPI del cliente a actualizar
    //Cliente cliente: objeto con los datos nuevos
    //Cliente retorna un objeto tipo CLiente ya actualizado

    //Metodo de tipo void para eliminar a un Cliente
    //void: no retorna ningun dato
    //Elimina un Cliente por su DPI
    void eliminar(String dpi);

    //boolean - retorna true si existe, false si no existe
    boolean existeDPI(String dpi);

    //Metodo para listar a los CLientes por su estado 0 o 1
    //Busca lista los usuarios dependiendo del numero
    List<Cliente> clienteEstado(int estado);





}
