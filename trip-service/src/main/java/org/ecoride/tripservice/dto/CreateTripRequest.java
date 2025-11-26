package org.ecoride.tripservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTripRequest {

    @NotBlank(message = "El origen es obligatorio")
    @Size(max = 500, message = "El origen no puede exceder 500 caracteres")
    private String origin;

    @NotBlank(message = "El destino es obligatorio")
    @Size(max = 500, message = "El destino no puede exceder 500 caracteres")
    private String destination;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Future(message = "La hora de inicio debe ser futura")
    private LocalDateTime startTime;

    @NotNull(message = "El número de asientos es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 asiento")
    @Max(value = 7, message = "No puede haber más de 7 asientos")
    private Integer seatsTotal;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @DecimalMax(value = "1000000.0", message = "El precio no puede exceder 1000000")
    private BigDecimal price;
}