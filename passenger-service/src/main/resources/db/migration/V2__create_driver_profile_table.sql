-- Tabla: driver_profile(id, passenger_id, license_no, car_plate, seats_offered, verification_status)

CREATE TABLE IF NOT EXISTS driver_profile (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      passenger_id UUID NOT NULL UNIQUE,
      license_no VARCHAR(50) NOT NULL UNIQUE,
      car_plate VARCHAR(20) NOT NULL UNIQUE,
      seats_offered INTEGER NOT NULL DEFAULT 1 CHECK (seats_offered BETWEEN 1 AND 8),
      verification_status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
          CHECK (verification_status IN ('PENDING', 'VERIFIED', 'REJECTED')),


      CONSTRAINT fk_driver_passenger FOREIGN KEY (passenger_id)
          REFERENCES passenger(id)
          ON DELETE CASCADE
);

CREATE INDEX idx_driver_passenger_id ON driver_profile(passenger_id);
CREATE INDEX idx_driver_verification_status ON driver_profile(verification_status);
CREATE INDEX idx_driver_license_no ON driver_profile(license_no);
CREATE INDEX idx_driver_car_plate ON driver_profile(car_plate);


COMMENT ON TABLE driver_profile IS 'Perfil de conductor extendido para pasajeros que ofrecen viajes';
COMMENT ON COLUMN driver_profile.passenger_id IS 'Referencia al pasajero base (1-1 relationship)';
COMMENT ON COLUMN driver_profile.license_no IS 'Número de licencia de conducir';
COMMENT ON COLUMN driver_profile.car_plate IS 'Placa del vehículo';
COMMENT ON COLUMN driver_profile.seats_offered IS 'Número de asientos disponibles (1-8)';
COMMENT ON COLUMN driver_profile.verification_status IS 'Estado de verificación: PENDING, VERIFIED, REJECTED';