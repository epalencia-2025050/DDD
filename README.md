# KinalApp - Sistema de Venta
KinalApp es una aplicación backend
desarrollada con Spring Boot y JPA/Hibernate 
para la gestión de ventas. Permite administrar clientes, usuarios, productos, 
ventas y detalles de venta, con una lógica de negocio que automatiza el cálculo de subtotales y totales.
## Características

- CRUD completo para Clientes, Usuarios, Productos, Ventas y DetalleVenta.
- Cálculo automático del subtotal de cada detalle (cantidad × precio unitario).
- Actualización automática del total de la venta al agregar, modificar o eliminar detalles.
- Validaciones básicas de datos.
- Arquitectura en capas (Controller, Service, Repository).

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Hibernate
- MySQL / PostgreSQL (configurable)
- Maven

## Requisitos previos

- JDK 21 o superior
- Maven 3.6+
- MySQL (u otro gestor de base de datos)
- Git
- Postman (para probar los endpoints)

## Instalacion y Ejecucion 

1. Clonar repositorio 
2. Abrir Intellij IDEA.
3.  Abrir la carpeta que clono.
4. Abrir MySQL en su ordenador.
5. Ingresar a la instancia activa en MySQL.
6. Regresar a Intellij IDEA.
7. Dirigirse a la carpeta "src\main\java\com\eduardoemilio".
8. Dirirgirse a KinalAppApplication y ejecutar la aplicación.
9. Abrir la carpeta "resources/application.properties".
10. Verificar que puerto esta utilizando la aplicación.
11. Abrir el navegador y poner el puerto http://localhost:8080/clientes.

## EndPoints 
1. code = DPI
2. Cliente:
3. "/clientes": Esto nos lista los clientes y agrega el cliente.
4. "/activos/estado": Esto lista los clientes que están activos.
5. "/{code}": Esto busca el cliente mediante él id.
6. "/{code}": Esto elimina el cliente mediante él id.
7. "/{code}": Esto actualiza el cliente mediante él id.

Usuario:

1. "/Usuarios": Esto nos lista los usuarios y agrega el usuario.
2. "/activos/estado": Esto lista los usuarios que están activos.
3. "/{code}": Esto busca el usuario mediante él id.
4. "/{code}": Esto elimina el usuario mediante él id.
5. "/{code}": Esto actualiza el usuario mediante él id.

Producto:

1. "/productos": Esto nos lista los productos y agrega el producto.
2. "/activos/estado": Esto lista los productos que están activos.
3. "/{code}": Esto busca el producto mediante él id.
4. "/{code}": Esto elimina el producto mediante él id.
5. "/{code}": Esto actualiza el producto mediante él id.
6. "/stock": Esto lista el nombre del producto y la cantidad que hay en stock.

Venta:

1. "/ventas": Esto nos lista las ventas y agrega la venta.
2. "/activos/estado": Esto lista las ventas que están activos.
3. "/{code}": Esto busca la venta mediante él id.
4. "/{code}": Esto elimina la venta mediante él id.
5. "/{code}": Esto actualiza la venta mediante él id.

DetalleVenta:

1. "/detallesVentas": Esto nos lista los detalles de venta y agrega el detalle venta.
2. "/activos/estado": Esto lista los detalles de venta que están activos.
3. "/{code}": Esto busca los detalles de venta mediante él id.
4. "/{code}": Esto elimina los detalles de venta mediante él id.
5. "/{code}": Esto actualiza los detalles de venta mediante él id.

## Postman del Proyecto

-Post de Clientes
{
"DPICliente": "1234567890123",
"nombreCliente": "Juan",
"apellidoCliente": "Pérez",
"direccion": "Calle Falsa 123",
"estado": 1
}

-Post de Usuario
{
"username": "vendedor1",
"password": "123456",
"email": "vendedor@tienda.com",
"rol": "VENDEDOR",
"estado": 1
}

-Post de Producto
{
"nombreProducto": "Laptop Gamer",
"precio": 1500.00,
"stock": 10,
"estado": 1
}

-Post de Venta
{
"fechaVenta": "2025-03-27",
"total": 12.00,
"estado": 1,
"cliente": {
"DPICliente": "1234567890123",
"estado": 1
},
"usuario": {
"codigoUsuario": 1,
"estado": 1
}
}

-Post de DetalleVenta
{
"cantidad": 2,
"precioUnitario": 1850.00,
"subtotal": 3700.00,
"producto": {
"codigoProducto": 1,
"estado": 1,
"stock": 15
},
"venta": {
"codigoVenta": 1,
"estado": 1
}
}

