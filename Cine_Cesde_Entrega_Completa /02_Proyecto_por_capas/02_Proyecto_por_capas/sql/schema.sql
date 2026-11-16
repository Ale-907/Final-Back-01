-- Crear la base por separado en phpMyAdmin y seleccionarla antes de importar.
-- No borra ni reemplaza tablas existentes. No usar sobre un esquema incompatible.
CREATE TABLE IF NOT EXISTS cliente (cedula VARCHAR(100) PRIMARY KEY, nombre VARCHAR(255) NOT NULL) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS pelicula (id INT PRIMARY KEY, titulo VARCHAR(255) NOT NULL, duracion INT NOT NULL, clasificacion VARCHAR(40) NOT NULL) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS combo (id INT PRIMARY KEY, nombre VARCHAR(255) NOT NULL, precio DOUBLE NOT NULL) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS cine (id INT PRIMARY KEY, nombre VARCHAR(255) NOT NULL) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS cine_pelicula (cine_id INT NOT NULL, pelicula_id INT NOT NULL, PRIMARY KEY(cine_id,pelicula_id), FOREIGN KEY(cine_id) REFERENCES cine(id) ON DELETE CASCADE, FOREIGN KEY(pelicula_id) REFERENCES pelicula(id)) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS sala (numero INT PRIMARY KEY, tipo VARCHAR(2) NOT NULL, dolby BOOLEAN, gafas VARCHAR(255)) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS asiento (sala_numero INT NOT NULL, codigo VARCHAR(100) NOT NULL, PRIMARY KEY(sala_numero,codigo), FOREIGN KEY(sala_numero) REFERENCES sala(numero) ON DELETE CASCADE) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS ticket (id INT PRIMARY KEY, cliente_id VARCHAR(100) NOT NULL, pelicula_id INT NOT NULL, sala_numero INT NOT NULL, asiento_codigo VARCHAR(100) NOT NULL, combo_id INT, precio DOUBLE NOT NULL, UNIQUE KEY asiento_vendido(sala_numero,asiento_codigo), FOREIGN KEY(cliente_id) REFERENCES cliente(cedula), FOREIGN KEY(pelicula_id) REFERENCES pelicula(id), FOREIGN KEY(sala_numero,asiento_codigo) REFERENCES asiento(sala_numero,codigo), FOREIGN KEY(combo_id) REFERENCES combo(id)) ENGINE=InnoDB;
