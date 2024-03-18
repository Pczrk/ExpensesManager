package com.example.proj1.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class NullArgumentException extends IllegalArgumentException{
    private final HttpStatus errorStatus = HttpStatus.BAD_REQUEST;
    private final String message = "At least one of the attributes is null";

    public ExceptionEntity buildEntity(){
        return ExceptionEntity.builder()
                .errorStatus(this.errorStatus)
                .message(this.message)
                .build();
    }
}
