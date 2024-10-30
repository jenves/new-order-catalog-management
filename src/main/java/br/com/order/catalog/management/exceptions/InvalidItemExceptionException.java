package br.com.order.catalog.management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidItemExceptionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidItemExceptionException(String message) {
        super(message);
    }
}