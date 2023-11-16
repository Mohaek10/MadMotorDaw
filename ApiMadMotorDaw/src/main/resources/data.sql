
INSERT INTO PIEZAS (id, nombre, descripcion, precio, stock, image)
VALUES
    (UUID(), 'Pieza 1', 'Descripción de la pieza 1', 19.99, 10, 'https://via.placeholder.com/150'),
    (UUID(), 'Pieza 2', 'Descripción de la pieza 2', 29.99, 5, 'https://via.placeholder.com/150'),
    (UUID(), 'Pieza 3', 'Descripción de la pieza 3', 39.99, 8, 'https://via.placeholder.com/150');


INSERT INTO PERSONAL (dni, nombre, apellidos, fecha_nacimiento, direccion, cuenta_bancaria)
VALUES
    ( '123456789', 'Nombre1', 'Apellido1', '1990-01-01', 'Dirección1', 'ES01234567890123456789'),
    ( '987654321', 'Nombre2', 'Apellido2', '1985-05-15', 'Dirección2', 'ES98765432109876543210'),
    ( '111223344', 'Nombre3', 'Apellido3', '1998-08-30', 'Dirección3', 'ES11112222333344445555');
INSERT INTO CATEGORIAS ( name)
VALUES
    ( 'AUTOMATICO'),
    ( 'GASOLINA'),
    ( 'HIBRIDO');

INSERT INTO VEHICULOS(id,marca, modelo,v_year,km,precio,stock,imagen,descripcion,categoria_id)
VALUES
    (UUID(),'AUDI','A7',2010,100000,50000,10,'https://via.placeholder.com/150','Descripcion1',1),
    (UUID(),'BMW','IX',2015,50000,60000,5,'https://via.placeholder.com/150','Descripcion2',2),
    (UUID(),'MERCEDES','BENZ',2018,20000,200000,8,'https://via.placeholder.com/150','Descripcion3',3),
    (UUID(),'SEAT','LEON',2019,10000,30000,10,'https://via.placeholder.com/150','Descripcion4',1);


INSERT INTO CLIENTES (id, nombre, apellido, direccion, codigo_Postal, dni, piezas, coches, imagen)
VALUES
    (uuid(),'Juan', 'Perez', '' 'Calle Principal', 12345, '12345678A', true, false, 'https://via.placeholder.com/150'),
    (UUID(), 'María', 'Gómez', 'Avenida Central', 54321, '98765432B', false, true, 'https://via.placeholder.com/150'),
    (UUID(), 'Pedro', 'Martínez', 'Calle Nueva', 67890, '45678901C', true, true, 'https://via.placeholder.com/150'),
    (UUID(), 'Laura', 'López', 'Paseo Grande', 13579, '01234567D', false, false, 'https://via.placeholder.com/150');
