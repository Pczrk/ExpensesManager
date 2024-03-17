package com.example.proj1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class UserCoreException extends RuntimeException{
    private final HttpStatus errorStatus;
    private String message;

    public UserExceptionEntity buildEntity(){
        return UserExceptionEntity.builder()
                .errorStatus(this.errorStatus)
                .message(this.message)
                .build();
    }

}
