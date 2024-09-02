package com.openpayd.exchange.Exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class GenericWebAccessException extends RuntimeException {

    private HttpStatus httpStatus;

    public GenericWebAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericWebAccessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
