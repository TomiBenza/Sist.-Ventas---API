package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.common.ErrorApi;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControllerExceptionHandlerTest {

    @Test
    public void testHandleErrorWithException() {
        Exception exception = new Exception("Test exception");
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ResponseEntity<ErrorApi> response = handler.handleError(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("Test exception", response.getBody().getMessage());
    }

    @Test
    public void testHandleErrorWithIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Test exception");
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ResponseEntity<ErrorApi> response = handler.handleError(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Test exception", response.getBody().getMessage());
    }

    @Test
    public void testHandleErrorWithMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult result = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "Test error message");

        when(exception.getBindingResult()).thenReturn(result);
        when(result.getFieldErrors()).thenReturn(List.of(fieldError));

        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ResponseEntity<ErrorApi> response = handler.handleError(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Test error message", response.getBody().getMessage());
    }

    @Test
    public void testHandleErrorWithResponseStatusException() {
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "Test reason");
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ResponseEntity<ErrorApi> response = handler.handleError(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Test reason", response.getBody().getMessage());
    }

    @Test
    public void testHandleErrorWithEntityNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException("Test exception");
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ResponseEntity<ErrorApi> response = handler.handleError(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Test exception", response.getBody().getMessage());
    }

}
