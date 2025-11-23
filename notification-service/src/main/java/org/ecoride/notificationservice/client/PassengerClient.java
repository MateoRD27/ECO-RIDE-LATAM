package org.ecoride.notificationservice.client;

import org.ecoride.notificationservice.dto.PassengerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "passenger-service")
public interface PassengerClient {
    @GetMapping("/passengers/{id}")
    PassengerDTO getPassenger(@PathVariable("id") UUID id);
}
