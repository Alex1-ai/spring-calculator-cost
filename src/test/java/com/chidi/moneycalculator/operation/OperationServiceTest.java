package com.chidi.moneycalculator.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OperationServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOperations() {
        // Arrange
        Operation operation1 = Operation.builder().id(1).type(OperationType.ADDITION).cost(10.0).build();
        Operation operation2 = Operation.builder().id(2).type(OperationType.SUBTRACTION).cost(10.0).build();
        List<Operation> operations = Arrays.asList(operation1, operation2);
        when(operationRepository.findAll()).thenReturn(operations);

        // Act
        List<Operation> result = operationService.getAllOperations();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(operations, result);
        verify(operationRepository, times(1)).findAll();
    }

    @Test
    void getOperationById_success() {
        // Arrange
        Integer id = 1;
        Operation operation = Operation.builder().id(id).type(OperationType.ADDITION).cost(10.0).build();
        when(operationRepository.findById(id)).thenReturn(Optional.of(operation));

        // Act
        Optional<Operation> result = operationService.getOperationById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(operation, result.get());
        verify(operationRepository, times(1)).findById(id);
    }

    @Test
    void getOperationById_notFound() {
        // Arrange
        Integer id = 1;
        when(operationRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Operation> result = operationService.getOperationById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(operationRepository, times(1)).findById(id);
    }

    @Test
    void saveOperation() {
        // Arrange
        Operation operation = Operation.builder().type(OperationType.ADDITION).cost(10.0).build();
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);

        // Act
        Operation result = operationService.saveOperation(operation);

        // Assert
        assertNotNull(result);
        assertEquals(operation, result);
        verify(operationRepository, times(1)).save(operation);
    }

    @Test
    void deleteOperation() {
        // Arrange
        Integer id = 1;
        doNothing().when(operationRepository).deleteById(id);

        // Act
        operationService.deleteOperation(id);

        // Assert
        verify(operationRepository, times(1)).deleteById(id);
    }
}
