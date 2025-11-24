-- Tabla: rating(id, trip_id, from_id, to_id, score, comment)
CREATE TABLE IF NOT EXISTS rating (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      trip_id UUID NOT NULL,
      from_id UUID NOT NULL,
      to_id UUID NOT NULL,
      score INTEGER NOT NULL CHECK (score BETWEEN 1 AND 5),
      comment TEXT,

      CONSTRAINT fk_rating_from_passenger FOREIGN KEY (from_id)
          REFERENCES passenger(id)
          ON DELETE CASCADE,

      CONSTRAINT fk_rating_to_passenger FOREIGN KEY (to_id)
          REFERENCES passenger(id)
          ON DELETE CASCADE,

      CONSTRAINT check_rating_not_self CHECK (from_id != to_id),
      CONSTRAINT unique_rating_per_trip UNIQUE (trip_id, from_id, to_id)
);

CREATE INDEX idx_rating_trip_id ON rating(trip_id);
CREATE INDEX idx_rating_from_id ON rating(from_id);
CREATE INDEX idx_rating_to_id ON rating(to_id);
CREATE INDEX idx_rating_score ON rating(score);


COMMENT ON TABLE rating IS 'Calificaciones entre pasajeros y conductores después de un viaje';
COMMENT ON COLUMN rating.trip_id IS 'ID del viaje (referencia a TripService)';
COMMENT ON COLUMN rating.from_id IS 'Pasajero que califica';
COMMENT ON COLUMN rating.to_id IS 'Pasajero calificado (puede ser conductor)';
COMMENT ON COLUMN rating.score IS 'Calificación de 1 a 5 estrellas';
COMMENT ON COLUMN rating.comment IS 'Comentario opcional sobre la experiencia';