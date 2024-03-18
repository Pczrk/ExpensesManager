package com.example.proj1.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserCoreException.class})
    protected ResponseEntity<ExceptionEntity> handleUserCoreException(UserCoreException e){
        return ResponseEntity.status(e.getErrorStatus()).body(e.buildEntity());
    }

    @ExceptionHandler({AuthenticationCoreException.class})
    protected ResponseEntity<ExceptionEntity> handleAuthenticationCoreException(AuthenticationCoreException e){
        return ResponseEntity.status(e.getErrorStatus()).body(e.buildEntity());
    }

    @ExceptionHandler({CrewCoreException.class})
    protected ResponseEntity<ExceptionEntity> handleCrewCoreException(CrewCoreException e){
        return ResponseEntity.status(e.getErrorStatus()).body(e.buildEntity());
    }

    @ExceptionHandler({NullArgumentException.class})
    protected ResponseEntity<ExceptionEntity> handleNullArgumentException(NullArgumentException e){
        return ResponseEntity.status(e.getErrorStatus()).body(e.buildEntity());
    }

    @ExceptionHandler({MemberCoreException.class})
    protected ResponseEntity<ExceptionEntity> handleMemberCoreException(MemberCoreException e){
        return ResponseEntity.status(e.getErrorStatus()).body(e.buildEntity());
    }

    @ExceptionHandler({TripCoreException.class})
    protected ResponseEntity<ExceptionEntity> handleTripCoreException(TripCoreException e){
        return ResponseEntity.status(e.getErrorStatus()).body(e.buildEntity());
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorDetails> handleAllExceptions(Exception e){
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ErrorDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .errorMessage(e.getMessage())
                        .build());
    }

    @Data
    @Builder
    public static class ErrorDetails implements Serializable{
        protected final LocalDateTime timestamp;
        protected final String errorMessage;
    }
}
