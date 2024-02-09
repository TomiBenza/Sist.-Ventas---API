package ar.edu.utn.frc.tup.lciv.controllers;
import ar.edu.utn.frc.tup.lciv.controllers.ControllerExceptionHandler;
import ar.edu.utn.frc.tup.lciv.dtos.common.ErrorApi;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ControllerExceptionHandlerTest {

    @Test
    public void testHandleErrorWithGeneralException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ResponseEntity<ErrorApi> response = handler.handleError(new Exception("Test Exception"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testHandleErrorWithIllegalArgumentException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ResponseEntity<ErrorApi> response = handler.handleError(new IllegalArgumentException("Test Exception"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testHandleErrorWithResponseStatusException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");

        ResponseEntity<ErrorApi> response = handler.handleError(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testHandleErrorWithEntityNotFoundException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ResponseEntity<ErrorApi> response = handler.handleError(new EntityNotFoundException("Test Exception"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
