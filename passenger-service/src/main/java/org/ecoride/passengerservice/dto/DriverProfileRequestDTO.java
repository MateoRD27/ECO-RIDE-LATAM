package org.ecoride.passengerservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverProfileRequestDTO {
    @NotBlank(message = "License number is required")
    @Size(max = 50)
    private String licenseNo;

    @NotBlank(message = "Car plate is required")
    @Size(max = 20)
    private String carPlate;

    @NotNull(message = "Seats offered is required")
    @Min(value = 1, message = "Must offer at least 1 seat")
    private Integer seatsOffered;
}