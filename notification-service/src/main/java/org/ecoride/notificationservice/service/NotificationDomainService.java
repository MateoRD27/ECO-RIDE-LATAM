package org.ecoride.notificationservice.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecoride.notificationservice.client.PassengerClient;
import org.ecoride.notificationservice.dto.PassengerDTO;
import org.ecoride.notificationservice.model.entities.NotificationLog;
import org.ecoride.notificationservice.model.enums.statusNotification;
import org.ecoride.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationDomainService {
    private final PassengerClient passengerClient;
    private final EmailService emailService;
    private final NotificationRepository notificationLogRepository;

    public void sendNotification(UUID passengerId, String subject, String messageContent) {
        String recipientEmail = "unknown";
        statusNotification status = statusNotification.FAILED;
        String error = null;

        try {
            // 1. Obtener datos del pasajero
            PassengerDTO passenger = passengerClient.getPassenger(passengerId);
            recipientEmail = passenger.getEmail();

            // Personalizar mensaje si es necesario
            String personalizedMessage = String.format("Hola %s,\n\n%s", passenger.getName(), messageContent);

            // 2. Enviar Correo
            emailService.sendEmail(recipientEmail, subject, personalizedMessage);
            status = statusNotification.SENT;

        } catch (Exception e) {
            log.error("Fallo en el proceso de notificación", e);
            error = e.getMessage();
        } finally {
            // 3. Guardar Log (Auditoría)
            NotificationLog logEntry = NotificationLog.builder()
                    .recipient(recipientEmail)
                    .subject(subject)
                    .content(messageContent)
                    .status(status)
                    .errorMessage(error)
                    .build();

            notificationLogRepository.save(logEntry);
        }
    }
}
