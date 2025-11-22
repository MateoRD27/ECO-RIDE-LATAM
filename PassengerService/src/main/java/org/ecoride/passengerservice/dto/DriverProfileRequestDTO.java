package org.ecoride.passengerservice.dto;

import lombok.Data;

@Data
public class DriverProfileRequestDTO {
    private String licenseNo;
    private String carPlate;
    private Integer seatsOffered;
}
