package org.ecoride.tripservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ecoride.tripservice.model.enums.TripStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID driverId;

    @Column(nullable = false, length = 500, columnDefinition = "varchar(500)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String origin;

    @Column(nullable = false, length = 500, columnDefinition = "varchar(500)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private Integer seatsTotal;

    @Column(nullable = false)
    private Integer seatsAvailable;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TripStatus status;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public boolean hasAvailableSeats() {
        return seatsAvailable > 0 && status == TripStatus.ACTIVE;
    }

    public void decrementSeats() {
        if (!hasAvailableSeats()) {
            throw new IllegalStateException("No hay asientos disponibles");
        }
        this.seatsAvailable--;
    }

    public void incrementSeats() {
        if (this.seatsAvailable >= this.seatsTotal) {
            throw new IllegalStateException("No se pueden incrementar más asientos");
        }
        this.seatsAvailable++;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setTrip(this);
    }
}