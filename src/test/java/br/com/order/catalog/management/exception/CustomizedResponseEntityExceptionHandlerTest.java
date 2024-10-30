package br.com.order.catalog.management.exception;

import br.com.order.catalog.management.exceptions.ExceptionResponse;
import br.com.order.catalog.management.exceptions.InvalidItemExceptionException;
import br.com.order.catalog.management.exceptions.ProductLinkedToOrderException;
import br.com.order.catalog.management.exceptions.ResourceNotFoundException;
import br.com.order.catalog.management.exceptions.handler.CustomizedResponseEntityExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomizedResponseEntityExceptionHandlerTest {

    private CustomizedResponseEntityExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new CustomizedResponseEntityExceptionHandler();
    }

    @Test
    void handleNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Product not found");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("Request made to fetch product with ID 123");

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleNotFoundExceptions(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody().getMessage());
        assertEquals("Request made to fetch product with ID 123", response.getBody().getDetails());
    }

    @Test
    void handleInvalidItemException() {
        InvalidItemExceptionException exception = new InvalidItemExceptionException("Invalid item in order");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("Attempt to add invalid item to order");

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleInvalidItemException(exception, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Invalid item in order", response.getBody().getMessage());
        assertEquals("Attempt to add invalid item to order", response.getBody().getDetails());
    }

    @Test
    void handleProductLinkedToOrderException() {
        ProductLinkedToOrderException exception = new ProductLinkedToOrderException("Product linked to an order");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("Attempt to delete product linked to an order");

        ResponseEntity<ExceptionResponse> response = exceptionHandler.handleProductLinkedToOrderException(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Product linked to an order", response.getBody().getMessage());
        assertEquals("Attempt to delete product linked to an order", response.getBody().getDetails());
    }
}
