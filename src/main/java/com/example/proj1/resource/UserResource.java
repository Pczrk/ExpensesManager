package com.example.proj1.resource;

import com.example.proj1.model.UserRequest;
import com.example.proj1.service.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.proj1.service.UserService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;
    private final SessionService sessionService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(
            @CookieValue(value = "Auth", required = false) String sessionId,
            @RequestBody UserRequest request,
            HttpServletResponse response){

        sessionService.noActiveSessionAssertion(sessionId);
        long id = userService.registerUser(request.getUserRegister());
        sessionService.createSession(id, response);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(
            @CookieValue(value = "Auth", required = false) String sessionId,
            @RequestBody UserRequest request,
            HttpServletResponse response){

        sessionService.noActiveSessionAssertion(sessionId);
        long id = userService.loginUser(request.getUserLogin());
        sessionService.createSession(id, response);

        return ResponseEntity.ok("Successfully logged in");
    }

    @DeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(
            @CookieValue(value = "Auth", required = false) String sessionId){
        sessionService.removeCurrentSession(sessionId);
        return ResponseEntity.ok("Successfully logged out");
    }

    @GetMapping(value = "/account")
    public ResponseEntity<Object> account(
            @CookieValue(value = "Auth", required = false) String sessionId){

        long userId = sessionService.validateSession(sessionId);

        return ResponseEntity.ok(userService.getUserById(userId));
    }

    //TODO @GetMapping(value = "/{id}")
}
