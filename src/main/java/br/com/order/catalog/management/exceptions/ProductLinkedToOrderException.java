package br.com.order.catalog.management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductLinkedToOrderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProductLinkedToOrderException(String message) {
        super(message);
    }
}