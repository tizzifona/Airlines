package projects.f5.airlines.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private UserAlreadyExistsException userAlreadyExistsException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException runtimeException = new RuntimeException("Some runtime error");

        ResponseEntity<String> response = globalExceptionHandler.handleRuntimeException(runtimeException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Some runtime error", response.getBody());
    }

    @Test
    void testHandleUserAlreadyExistsException() {

        String errorMessage = "User already exists";
        when(userAlreadyExistsException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<String> response = globalExceptionHandler
                .handleUserAlreadyExistsException(userAlreadyExistsException);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
}
