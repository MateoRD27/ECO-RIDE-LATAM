package org.ecoride.passengerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ecoride.passengerservice.dto.PassengerResponseDTO;
import org.ecoride.passengerservice.service.PassengerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passengers")
@RequiredArgsConstructor
@Tag(name = "Passenger", description = "Passenger management endpoints") //Swagger
@SecurityRequirement(name = "bearer-jwt") //Swagger indica que requiere token JWT para usarlo
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping("/me")
    @Operation(summary = "Get current passenger profile", description = "Retrieve profile by Keycloak sub")
    public ResponseEntity<PassengerResponseDTO> getMe(@AuthenticationPrincipal Jwt jwt) {
        String keycloakSub = jwt.getSubject();
        String name = jwt.getClaimAsString("name");
        String email = jwt.getClaimAsString("email");

        PassengerResponseDTO response = passengerService.createPassengerIfNotExists(keycloakSub, name, email);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get passenger by ID", description = "Internal endpoint for other services")
    public ResponseEntity<PassengerResponseDTO> getPassengerById(@PathVariable java.util.UUID id) {
        PassengerResponseDTO passenger = passengerService.getPassengerById(id); // Necesitamos crear este método en el service
        return ResponseEntity.ok(passenger);
    }
}