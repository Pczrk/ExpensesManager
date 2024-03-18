package com.example.proj1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class CrewCoreException extends RuntimeException{
    private final HttpStatus errorStatus;
    private String message;

    public ExceptionEntity buildEntity(){
        return ExceptionEntity.builder()
                .errorStatus(this.errorStatus)
                .message(this.message)
                .build();
    }

}
