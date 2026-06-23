-- =====================================================
--  SiReSe — Sistema de Registro de Aspirantes
--  Base de Datos MySQL
--  UPIIZ - IPN | Tecnologías para Web 2026
-- =====================================================

CREATE DATABASE IF NOT EXISTS sirese_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE sirese_db;

-- -----------------------------------------------------
--  Tabla: carreras
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS carreras (
  id            BIGINT        NOT NULL AUTO_INCREMENT,
  nombre        VARCHAR(150)  NOT NULL,
  semestres     INT           NOT NULL,
  observaciones TEXT,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos iniciales
INSERT INTO carreras (nombre, semestres, observaciones) VALUES
  ('Ingeniería en Sistemas Computacionales', 10, 'Titulación por proyecto o tesis.'),
  ('Mecatrónica',                            10, 'Enfoque en automatización industrial.'),
  ('Inteligencia Artificial',                 8, 'Área de vanguardia tecnológica.'),
  ('Ingeniería en Alimentos',                 8, 'Orientada a industria alimentaria.');

-- -----------------------------------------------------
--  Tabla: aspirantes
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS aspirantes (
  id               BIGINT       NOT NULL AUTO_INCREMENT,
  nombre           VARCHAR(200) NOT NULL,
  telefono         VARCHAR(10)  NOT NULL,
  email            VARCHAR(150) NOT NULL UNIQUE,
  carrera_id       BIGINT       NOT NULL,
  fecha_registro   DATE         NOT NULL DEFAULT (CURRENT_DATE),
  PRIMARY KEY (id),
  CONSTRAINT fk_aspirante_carrera
    FOREIGN KEY (carrera_id) REFERENCES carreras(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  INDEX idx_email (email),
  INDEX idx_carrera (carrera_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos de ejemplo
INSERT INTO aspirantes (nombre, telefono, email, carrera_id, fecha_registro) VALUES
  ('María García López',     '4921112233', 'maria.garcia@example.com',   1, '2026-01-10'),
  ('Juan Martínez Pérez',    '4923334455', 'juan.martinez@example.com',  2, '2026-01-11'),
  ('Ana González Fernández', '4925556677', 'ana.gonzalez@example.com',   3, '2026-01-12'),
  ('Luis Rodríguez Gómez',   '4927778899', 'luis.rodriguez@example.com', 4, '2026-01-13'),
  ('Carmen Sánchez Ruiz',    '4929990011', 'carmen.sanchez@example.com', 2, '2026-01-14');

-- -----------------------------------------------------
--  Tabla: administradores
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS administradores (
  id               BIGINT       NOT NULL AUTO_INCREMENT,
  usuario          VARCHAR(50)  NOT NULL UNIQUE,
  contrasena       VARCHAR(255) NOT NULL,  -- BCrypt en producción
  nombre_completo  VARCHAR(200) NOT NULL,
  email            VARCHAR(150) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Administrador inicial (contraseña: admin — en producción usar hash BCrypt)
INSERT INTO administradores (usuario, contrasena, nombre_completo, email) VALUES
  ('admin', 'admin', 'Administrador del Sistema', 'admin@upiiz.ipn.mx');

-- -----------------------------------------------------
--  Vista: vista_aspirantes (útil para reportes)
-- -----------------------------------------------------
CREATE OR REPLACE VIEW vista_aspirantes AS
SELECT
  a.id,
  a.nombre,
  a.telefono,
  a.email,
  c.nombre  AS carrera,
  c.semestres,
  a.fecha_registro AS fecha
FROM aspirantes a
JOIN carreras c ON a.carrera_id = c.id
ORDER BY a.fecha_registro DESC;

-- -----------------------------------------------------
--  Consultas útiles para el proyecto
-- -----------------------------------------------------
-- Ver todos los aspirantes con su carrera:
--   SELECT * FROM vista_aspirantes;

-- Verificar si un correo ya existe:
--   SELECT COUNT(*) FROM aspirantes WHERE email = 'correo@ejemplo.com';

-- Conteo por carrera:
--   SELECT c.nombre, COUNT(a.id) AS total
--   FROM carreras c LEFT JOIN aspirantes a ON c.id = a.carrera_id
--   GROUP BY c.id, c.nombre;

-- -----------------------------------------------------
--  Tabla: constancias  (historial de constancias emitidas)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS constancias (
  id            BIGINT   NOT NULL AUTO_INCREMENT,
  aspirante_id  BIGINT   NOT NULL,
  fecha_emision DATE     NOT NULL DEFAULT (CURRENT_DATE),
  PRIMARY KEY (id),
  CONSTRAINT fk_constancia_aspirante
    FOREIGN KEY (aspirante_id) REFERENCES aspirantes(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  INDEX idx_constancia_aspirante (aspirante_id),
  INDEX idx_constancia_fecha (fecha_emision)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
