package org.ecoride.notificationservice.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.ecoride.notificationservice.model.enums.statusNotification;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "notification_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String recipient; // El email destino

    @Column(nullable = false)
    private String subject;   // El asunto del correo

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;   // El cuerpo del mensaje

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private statusNotification status;    // SENT o FAILED

    @Column(columnDefinition = "TEXT")
    private String errorMessage; // Si falló, aquí guardamos el error

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime sentAt; // Cuándo se intentó enviar
}
