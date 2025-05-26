-- Eliminar tablas si existen previamente
DROP DATABASE IF EXISTS BlackjackDB;
DROP TABLE IF EXISTS ManoJugador;
DROP TABLE IF EXISTS Cartas;
DROP TABLE IF EXISTS Partidas;
DROP TABLE IF EXISTS Jugadores;

-- Crear la base de datos
CREATE DATABASE BlackjackDB;
USE BlackjackDB;

-- Tabla de jugadores con sistema de dinero y contraseña
CREATE TABLE Jugadores (
    id_jugador INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    contraseña VARCHAR(100) NOT NULL, 
    dinero INT DEFAULT 100
);

-- Partidas jugadas por el jugador
CREATE TABLE Partidas (
    id_partida INT PRIMARY KEY AUTO_INCREMENT,
    id_jugador INT NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_apostado INT DEFAULT 0,
    total_ganado INT DEFAULT 0,
    total_perdido INT DEFAULT 0,
    dinero_actual INT DEFAULT 0,
    estado VARCHAR(50) DEFAULT 'En curso',
    FOREIGN KEY (id_jugador) REFERENCES Jugadores(id_jugador) ON DELETE CASCADE
);

-- Cartas usadas en el juego (sin imágenes, con valores correctos)
CREATE TABLE Cartas (
    id_carta INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL, 
    valor INT NOT NULL 
);

-- Insertar cartas con valores reales en Blackjack
INSERT INTO Cartas (nombre, valor) VALUES
('As de Corazones', 11), ('As de Diamantes', 11), ('As de Picas', 11), ('As de Tréboles', 11),
('2 de Corazones', 2), ('2 de Diamantes', 2), ('2 de Picas', 2), ('2 de Tréboles', 2),
('3 de Corazones', 3), ('3 de Diamantes', 3), ('3 de Picas', 3), ('3 de Tréboles', 3),
('4 de Corazones', 4), ('4 de Diamantes', 4), ('4 de Picas', 4), ('4 de Tréboles', 4),
('5 de Corazones', 5), ('5 de Diamantes', 5), ('5 de Picas', 5), ('5 de Tréboles', 5),
('6 de Corazones', 6), ('6 de Diamantes', 6), ('6 de Picas', 6), ('6 de Tréboles', 6),
('7 de Corazones', 7), ('7 de Diamantes', 7), ('7 de Picas', 7), ('7 de Tréboles', 7),
('8 de Corazones', 8), ('8 de Diamantes', 8), ('8 de Picas', 8), ('8 de Tréboles', 8),
('9 de Corazones', 9), ('9 de Diamantes', 9), ('9 de Picas', 9), ('9 de Tréboles', 9),
('10 de Corazones', 10), ('10 de Diamantes', 10), ('10 de Picas', 10), ('10 de Tréboles', 10),
('Jota de Corazones', 10), ('Jota de Diamantes', 10), ('Jota de Picas', 10), ('Jota de Tréboles', 10),
('Reina de Corazones', 10), ('Reina de Diamantes', 10), ('Reina de Picas', 10), ('Reina de Tréboles', 10),
('Rey de Corazones', 10), ('Rey de Diamantes', 10), ('Rey de Picas', 10), ('Rey de Tréboles', 10);

-- Mano del jugador en cada partida
CREATE TABLE ManoJugador (
    id_mano INT PRIMARY KEY AUTO_INCREMENT,
    id_partida INT NOT NULL,
    id_jugador INT NOT NULL,
    id_carta INT NOT NULL,
    FOREIGN KEY (id_partida) REFERENCES Partidas(id_partida) ON DELETE CASCADE,
    FOREIGN KEY (id_jugador) REFERENCES Jugadores(id_jugador) ON DELETE CASCADE,
    FOREIGN KEY (id_carta) REFERENCES Cartas(id_carta) ON DELETE CASCADE
);



