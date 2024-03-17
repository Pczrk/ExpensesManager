package com.example.proj1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class AuthenticationCoreException extends RuntimeException{
    private final HttpStatus errorStatus;
    private String message;

    public AuthenticationExceptionEntity buildEntity(){
        return AuthenticationExceptionEntity.builder()
                .errorStatus(this.errorStatus)
                .message(this.message)
                .build();
    }

}
