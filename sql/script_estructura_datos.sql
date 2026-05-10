-- ============================================================
-- CheckPoint — Script de creación de base de datos
-- Raquel González Carranza — DAM2 2025-26
-- ============================================================

CREATE DATABASE IF NOT EXISTS videojuegos_db 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE videojuegos_db;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id               INT          AUTO_INCREMENT,
    username         VARCHAR(50)  UNIQUE NOT NULL,
    password_hash    VARCHAR(255) NOT NULL,
    email            VARCHAR(100) UNIQUE NOT NULL,
    nombre           VARCHAR(100),
    ultima_conexion  DATETIME,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de plataformas
CREATE TABLE IF NOT EXISTS plataformas (
    id         INT          AUTO_INCREMENT,
    nombre     VARCHAR(100) NOT NULL UNIQUE,
    rawg_slug  VARCHAR(100) NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos iniciales de plataformas
INSERT INTO plataformas (nombre, rawg_slug) VALUES
    ('PC',                'pc'),
    ('PlayStation 5',     'playstation5'),
    ('PlayStation 4',     'playstation4'),
    ('PlayStation 3',     'playstation3'),
    ('PlayStation 2',     'playstation2'),
    ('PlayStation',       'playstation'),
    ('Xbox Series X/S',   'xbox-series-x'),
    ('Xbox One',          'xbox-one'),
    ('Nintendo Switch',   'nintendo-switch'),
    ('Nintendo Switch 2', NULL),
    ('Nintendo',          'nes'),
    ('Nintendo 3DS',      'nintendo-3ds'),
    ('PSP',               'psp'),
    ('PS Vita',           'ps-vita');

-- Tabla de videojuegos
CREATE TABLE IF NOT EXISTS videojuegos (
    id          INT             AUTO_INCREMENT,
    titulo      VARCHAR(150)    NOT NULL,
    plataforma  VARCHAR(100)    NOT NULL,
    anio        VARCHAR(50),
    valoracion  DECIMAL(3,1),
    favorito    TINYINT(1)      NOT NULL DEFAULT 0,
    genero      VARCHAR(100)    NULL,
    imagen_url  VARCHAR(500)    NULL,
    anotacion   TEXT            NULL,
    usuario_id  INT             NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT videojuegos_ibfk_1
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;