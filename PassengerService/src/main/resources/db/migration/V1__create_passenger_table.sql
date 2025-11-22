-- Tabla: passenger(id, keycloak_sub, name, email, rating_avg, created_at)

-- Habilita la extensión pgcrypto para permitir la generación de UUIDs seguros con gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS passenger (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    keycloak_sub VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    rating_avg DECIMAL(3,2) DEFAULT 0.00 CHECK (rating_avg >= 0 AND rating_avg <= 5),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX idx_passenger_keycloak_sub ON passenger(keycloak_sub);
CREATE INDEX idx_passenger_email ON passenger(email);
CREATE INDEX idx_passenger_created_at ON passenger(created_at);

COMMENT ON TABLE passenger IS 'Pasajeros registrados en el sistema';
COMMENT ON COLUMN passenger.keycloak_sub IS 'Subject ID de Keycloak (UUID del usuario)';
COMMENT ON COLUMN passenger.rating_avg IS 'Promedio de calificaciones recibidas (0-5)';
COMMENT ON COLUMN passenger.created_at IS 'Fecha de registro del pasajero';
