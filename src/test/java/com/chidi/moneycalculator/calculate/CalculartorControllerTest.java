package com.chidi.moneycalculator.calculate;

import com.chidi.moneycalculator.config.JwtService;
import com.chidi.moneycalculator.record.RecordDTO;
import com.chidi.moneycalculator.operation.OperationType;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CalculartorControllerTest {

    @Mock
    private CalculationService calculationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CalculartorController calculartorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateSuccess() {
        // Arrange
        String token = "Bearer validToken";
        String userEmail = "user@example.com";
        CalculationRequest calculationRequest = new CalculationRequest();
        calculationRequest.setOperationType(OperationType.ADDITION);
        calculationRequest.setFirstNum(1.0);
        calculationRequest.setSecondNum(2.0);

        RecordDTO recordDTO = new RecordDTO();
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUsername(anyString())).thenReturn(userEmail);
        when(calculationService.performOperation(eq(userEmail), eq(OperationType.ADDITION), eq(1.0), eq(2.0)))
                .thenReturn(Optional.of(recordDTO));

        // Act
        ResponseEntity<?> responseEntity = calculartorController.calculate(calculationRequest, request);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(recordDTO, responseEntity.getBody());
    }

    @Test
    void testCalculateUnauthorized() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        CalculationRequest calculationRequest = new CalculationRequest();

        // Act
        ResponseEntity<?> responseEntity = calculartorController.calculate(calculationRequest, request);

        // Assert
        assertEquals(401, responseEntity.getStatusCodeValue());
        assertEquals("Authorization header is missing or invalid.", responseEntity.getBody());
    }

    @Test
    void testCalculateBadRequest() {
        // Arrange
        String token = "Bearer validToken";
        String userEmail = "user@example.com";
        CalculationRequest calculationRequest = new CalculationRequest();
        calculationRequest.setOperationType(OperationType.ADDITION);
        calculationRequest.setFirstNum(1.0);
        calculationRequest.setSecondNum(2.0);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUsername(anyString())).thenReturn(userEmail);
        when(calculationService.performOperation(eq(userEmail), eq(OperationType.ADDITION), eq(1.0), eq(2.0)))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> responseEntity = calculartorController.calculate(calculationRequest, request);

        // Assert
        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Calculation failed. Either user not found or insufficient balance.", responseEntity.getBody());
    }
}
