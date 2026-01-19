-- DIEGO HUMBERTO CASA PINOS
-- Reto técnico SOFKA

-- 0 CREAR BDD 
-- CREATE DATABASE sofka_db;

-- 1 PERSONA
CREATE TABLE persona (
	persona_id	BIGSERIAL PRIMARY KEY,
    nombre	VARCHAR(100) NOT NULL,
    genero	VARCHAR(20),
    edad	INTEGER CHECK (edad >= 0),
    identificacion	VARCHAR(20) NOT NULL UNIQUE,
    direccion	VARCHAR(150),
    telefono	VARCHAR(20)
);

-- 2 CLIENTE
CREATE TABLE cliente (
    cliente_id	BIGSERIAL PRIMARY KEY,
    persona_id	BIGINT NOT NULL UNIQUE,
    contrasena	VARCHAR(100) NOT NULL,
    estado	BOOLEAN NOT NULL,
    constraint	fk_cliente_persona
        FOREIGN KEY (persona_id)
        REFERENCES persona (persona_id)
        ON DELETE CASCADE
);

-- 3 CUENTA
CREATE TABLE cuenta (
    cuenta_id	BIGSERIAL PRIMARY KEY,
    numero_cuenta	VARCHAR(20) NOT NULL UNIQUE,
    tipo_cuenta	VARCHAR(20) NOT NULL,
    saldo_inicial	NUMERIC(12,2) NOT NULL CHECK (saldo_inicial >= 0),
    estado	BOOLEAN NOT NULL,
    cliente_id	BIGINT NOT NULL,
    CONSTRAINT fk_cuenta_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente (cliente_id)
);

-- MOVIMIENTO
CREATE TABLE movimiento (
    movimiento_id       BIGSERIAL PRIMARY KEY,
    fecha               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento     VARCHAR(20) NOT NULL,
    valor               NUMERIC(12,2) NOT NULL,
    saldo_disponible    NUMERIC(12,2) NOT NULL,
    cuenta_id           BIGINT NOT NULL,
    CONSTRAINT fk_movimiento_cuenta
        FOREIGN KEY (cuenta_id)
        REFERENCES cuenta (cuenta_id)
);