package ar.edu.utn.frc.tup.lciv.dtos.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorApiTest {

    @Test
    void testErrorApiConstructorAndGetters() {
        // Arrange
        String timestamp = "2023-06-16 10:00:00";
        Integer status = 404;
        String error = "Not Found";
        String message = "Resource not found";

        // Act
        ErrorApi errorApi = new ErrorApi(timestamp, status, error, message);

        // Assert
        assertEquals(timestamp, errorApi.getTimestamp());
        assertEquals(status, errorApi.getStatus());
        assertEquals(error, errorApi.getError());
        assertEquals(message, errorApi.getMessage());
    }

    @Test
    void testErrorApiBuilderAndGetters() {
        // Arrange
        String timestamp = "2023-06-16 10:00:00";
        Integer status = 404;
        String error = "Not Found";
        String message = "Resource not found";

        // Act
        ErrorApi errorApi = ErrorApi.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message)
                .build();

        // Assert
        assertEquals(timestamp, errorApi.getTimestamp());
        assertEquals(status, errorApi.getStatus());
        assertEquals(error, errorApi.getError());
        assertEquals(message, errorApi.getMessage());
    }
}
