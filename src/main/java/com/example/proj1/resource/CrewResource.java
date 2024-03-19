package com.example.proj1.resource;

import com.example.proj1.model.CrewRequest;
import com.example.proj1.model.CrewResponse;
import com.example.proj1.service.SessionService;
import com.example.proj1.service.CrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/crews")
@RequiredArgsConstructor
public class CrewResource {

    private final SessionService sessionService;
    private final CrewService crewService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createCrew(
            @CookieValue (value = "Auth", required = false) String sessionId,
            @RequestBody CrewRequest request){
        long userId = sessionService.validateSession(sessionId);
        long crewId = crewService.createCrew(request.getCrewCreate(),userId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/crews/{id}")
                .buildAndExpand(crewId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getCrew(
            @CookieValue (value = "Auth", required = false) String sessionId,
            @PathVariable (value = "id") Long crewId){
        long userId = sessionService.validateSession(sessionId);
        return ResponseEntity.ok(crewService.getCrew(crewId,userId));
    }

    @GetMapping(value = "/my-crews")
    public ResponseEntity<Object> getMyCrews(@CookieValue (value = "Auth", required = false) String sessionId){
        long userId = sessionService.validateSession(sessionId);
        return ResponseEntity.ok(new CrewResponse(crewService.getUserCrews(userId)));
    }

    @PostMapping(value = "/join/{access-key}")
    public ResponseEntity<Object> joinCrew(
            @CookieValue (value = "Auth", required = false) String sessionId,
            @PathVariable (value = "access-key") String accessKey){
        long userId = sessionService.validateSession(sessionId);
        crewService.joinCrew(userId, accessKey);
        return ResponseEntity.ok("Successfully joined crew");
    }
}
