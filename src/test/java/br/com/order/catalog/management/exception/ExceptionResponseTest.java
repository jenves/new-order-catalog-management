package br.com.order.catalog.management.exception;

import br.com.order.catalog.management.exceptions.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionResponseTest {

    private ExceptionResponse exceptionResponse;
    private Date timestamp;
    private String message;
    private String details;

    @BeforeEach
    void setUp() {
        timestamp = new Date();
        message = "Test error message";
        details = "Test error details";
        exceptionResponse = new ExceptionResponse(timestamp, message, details);
    }

    @Test
    void testGetTimestamp() {
        assertEquals(timestamp, exceptionResponse.getTimestamp());
    }

    @Test
    void testGetMessage() {
        assertEquals(message, exceptionResponse.getMessage());
    }

    @Test
    void testGetDetails() {
        assertEquals(details, exceptionResponse.getDetails());
    }

    @Test
    void testSetTimestamp() {
        Date newTimestamp = new Date();
        exceptionResponse.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, exceptionResponse.getTimestamp());
    }

    @Test
    void testSetMessage() {
        String newMessage = "New test error message";
        exceptionResponse.setMessage(newMessage);
        assertEquals(newMessage, exceptionResponse.getMessage());
    }

    @Test
    void testSetDetails() {
        String newDetails = "New test error details";
        exceptionResponse.setDetails(newDetails);
        assertEquals(newDetails, exceptionResponse.getDetails());
    }
}
