package com.openpayd.exchange.Exception;

import org.springframework.dao.DataAccessException;

public class GenericDataAccessException extends RuntimeException {

    public GenericDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
