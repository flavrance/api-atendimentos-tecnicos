package com.technicalsupport.api.handlers;

import com.technicalsupport.api.models.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private ConstraintViolation<String> constraintViolation;

    @Mock
    private Path propertyPath;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationExceptions_MustReturnStatus422() {
        // Arrange
        FieldError fieldError = new FieldError("object", "field", "error message");
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.Collections.singletonList(fieldError));

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleValidationExceptions(methodArgumentNotValidException);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getBody().getStatus());
    }

    @Test
    void handleConstraintViolation_MustReturnStatus400() {
        // Arrange
        Set<ConstraintViolation<String>> violations = new HashSet<>();
        violations.add(constraintViolation);
        when(constraintViolation.getPropertyPath()).thenReturn(propertyPath);
        when(propertyPath.toString()).thenReturn("field");
        when(constraintViolation.getMessage()).thenReturn("message error");
        
        ConstraintViolationException ex = new ConstraintViolationException("Constraint Validation", violations);

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleConstraintViolation(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    void handleMissingHeader_MustReturnStatus400() {
        // Arrange
        MissingRequestHeaderException ex = new MissingRequestHeaderException("CPF", null);

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleMissingHeader(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getErrors().toString().contains("CPF"));
    }

    @Test
    void handleIllegalArgument_MustReturnStatus400() {
        // Arrange
        IllegalArgumentException ex = new IllegalArgumentException("Invalid Argument");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Invalid Argument", response.getBody().getErrors());
    }

    @Test
    void handleGenericException_MustReturnStatus500() {
        // Arrange
        Exception ex = new Exception("Generic Error");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getErrors());
    }
} 