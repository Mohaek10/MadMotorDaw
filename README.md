# Proyecto API REST Concesionario de Vehículos

¡Bienvenido al proyecto de API REST para un concesionario de vehículos desarrollado por el equipo **ApiMadMotorDAW**!

***AUTORES***: Rubén Fernández Pérez, Mohamed El Kasmi El Kaderi, Miguel Vicario Rubio, Joe Brandon Carrillo Lozano.



## Descripción del Proyecto

Este proyecto implementa una API REST utilizando Spring Boot para gestionar un concesionario de vehículos. La API ofrece operaciones CRUD (Crear, Leer, Actualizar, Borrar) para entidades como vehículos, categorías, clientes, pedidos, personal, piezas, usuarios, y más.

## Estructura del Proyecto

El proyecto sigue una estructura modularizada que facilita la comprensión y mantenimiento del código. A continuación, se presenta una visión general de la estructura:

- **config**: Contiene configuraciones específicas del proyecto, como configuraciones de seguridad, CORS, almacenamiento y Swagger.
- **rest**: Agrupa los controladores REST para cada entidad, junto con DTOs, excepciones, mappers, modelos, repositorios y servicios correspondientes.
- **utils**: Contiene utilidades generales, como clases para la paginación de resultados.
- **websockets**: Implementación de websockets para notificaciones, con modelos, DTOs y mappers específicos.

## Controladores Destacados

- **VehiculoRestController**: Controlador para gestionar operaciones relacionadas con vehículos, incluyendo búsqueda, creación, actualización, eliminación y manipulación de imágenes.

## Configuraciones y Propiedades

- **application.properties**: Archivo de configuración principal.
- **application-dev.properties** y **application-prod.properties**: Archivos de configuración específicos para entornos de desarrollo y producción.
- **cert/server_keystore.p12**: Almacén de claves para configuración de seguridad (SSL).

## Perfiles de Seguridad

La seguridad se ha implementado utilizando Spring Security, con roles de usuario y autenticación JWT.

## Pruebas

El directorio de pruebas (`test`) contiene suites de pruebas para cada módulo del proyecto, utilizando JUnit y Mockito para garantizar la calidad del código.

## Despliegue

El proyecto está configurado para ser desplegado en entornos de desarrollo y producción, con propiedades específicas para cada entorno.

## Ejecución Local

1. Clona el repositorio.
2. Configura la base de datos y las propiedades de la aplicación según tus necesidades en los archivos de propiedades.
3. Ejecuta la aplicación utilizando tu entorno de desarrollo preferido.


## Problemas Conocidos

Actualmente hay un problema conocido. Al crear un pedido no se calcula bien el total. Si encuentras algun error, por favor, informa a nuestro equipo. elkasmimoha@gmail.com

## Contacto

Para cualquier pregunta o comentario, no dudes en ponerte en contacto con nosotros. ¡Estamos aquí para ayudar!
atencionalcliente@madmotor.es

---

¡Esperamos que disfrutes trabajando con nuestra API REST para el concesionario de vehículos! ¡Gracias por ser parte de ApiMadMotorDAW!
