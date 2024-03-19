package com.example.proj1.resource;

import com.example.proj1.model.ExpenseRequest;
import com.example.proj1.model.ExpenseResponse;
import com.example.proj1.model.TripRequest;
import com.example.proj1.service.SessionService;
import com.example.proj1.service.ExpenseService;
import com.example.proj1.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripResource {

    private final SessionService sessionService;
    private final TripService tripService;
    private final ExpenseService expenseService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createTrip(
            @CookieValue(name = "Auth", required = false) String sessionId,
            @RequestBody TripRequest request){
        long userId = sessionService.validateSession(sessionId);

        long tripId = tripService.createTrip(request.getTrip(), userId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/trips/{id}")
                .buildAndExpand(tripId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getTrip(
            @CookieValue(name = "Auth", required = false) String sessionId,
            @PathVariable(value = "id") Long tripId){
        Long userId = sessionService.validateSession(sessionId);
        return ResponseEntity.ok(tripService.getTrip(tripId, userId));
    }

    //might be moved to ExpensesResource
    @GetMapping(value = "/{id}/expenses")
    public ResponseEntity<Object> getExpenses(
            @CookieValue(name = "Auth", required = false) String sessionId,
            @PathVariable(value = "id") Long tripId){
        Long userId = sessionService.validateSession(sessionId);
        return ResponseEntity.ok(new ExpenseResponse(expenseService.getAllExpenses(tripId, userId)));
    }

    //might be moved to ExpensesResource
    @PostMapping(value = "/add-expense", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addExpense(
            @CookieValue(name = "Auth", required = false) String sessionId,
            @RequestBody ExpenseRequest request){
        Long userId = sessionService.validateSession(sessionId);

        Long expenseId = expenseService.createExpense(request.getExpense(), userId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/trips/{tripId}/expense/{expenseId}")
                .buildAndExpand(request.getExpense().getTripId(), expenseId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

}
