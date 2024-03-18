package com.example.proj1.resource;

import com.example.proj1.model.TripRequest;
import com.example.proj1.service.AuthenticationService;
import com.example.proj1.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/trip")
@RequiredArgsConstructor
public class TripResource {
    private final AuthenticationService authenticationService;
    private final TripService tripService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createTrip(
            @CookieValue(name = "Auth", required = false) String sessionId,
            @RequestBody TripRequest request){
        long userId = authenticationService.validateSession(sessionId);

        long tripId = tripService.createTrip(request.getTrip(), userId);

        URI location = ServletUriComponentsBuilder
                .fromPath("/trip")
                .path("/{id}")
                .buildAndExpand(tripId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getTrip(
            @CookieValue(name = "Auth", required = false) String sessionId,
            @PathVariable(value = "id") Long tripId){
        Long userId = authenticationService.validateSession(sessionId);

        return ResponseEntity.ok(tripService.getTrip(tripId, userId));
    }
}
