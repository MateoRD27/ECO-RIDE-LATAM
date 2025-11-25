package org.ecoride.tripservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ecoride.tripservice.model.enums.ReservationStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID passengerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public void confirm() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden confirmar reservas pendientes");
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel(String reason) {
        if (this.status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("La reserva ya está cancelada");
        }
        this.status = ReservationStatus.CANCELLED;
    }
}