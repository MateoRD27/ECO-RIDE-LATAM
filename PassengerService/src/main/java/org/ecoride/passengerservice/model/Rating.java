package org.ecoride.passengerservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "rating", uniqueConstraints = @UniqueConstraint(name = "unique_rating_per_trip", columnNames = {"trip_id","from_id","to_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;


    @Column(name = "trip_id", nullable = false)
    private UUID tripId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id", nullable = false)
    private Passenger fromPassenger;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id", nullable = false)
    private Passenger toPassenger;


    @Column(nullable = false)
    private Integer score;


    @Column(columnDefinition = "TEXT")
    private String comment;


    /**
     * Regla de dominio: un pasajero no puede calificarse a sí mismo
     * Se valida antes de la persistencia para mantener la integridad.
     */
    @PrePersist
    public void validateNotSelf() {
        if (fromPassenger != null && toPassenger != null &&
                fromPassenger.getId() != null && fromPassenger.getId().equals(toPassenger.getId())) {
            throw new IllegalStateException("A passenger cannot rate themselves");
        }
    }
}