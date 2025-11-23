package org.ecoride.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PassengerDTO {
    private UUID id;
    private String name;
    private String email;
}
