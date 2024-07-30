package com.chidi.moneycalculator.calculate;

import com.chidi.moneycalculator.operation.Operation;
import com.chidi.moneycalculator.operation.OperationRepository;
import com.chidi.moneycalculator.operation.OperationType;
import com.chidi.moneycalculator.record.Record;
import com.chidi.moneycalculator.record.RecordDTO;
import com.chidi.moneycalculator.record.RecordRepository;
import com.chidi.moneycalculator.user.User;
import com.chidi.moneycalculator.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CalculationServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CalculationService calculationService;

    private static final String USER_EMAIL = "user@example.com";
    private static final double FIXED_COST = 10.0;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void performOperation_addition_success() {
        // Arrange
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setBalance(50.0);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(operationRepository.save(any(Operation.class))).thenAnswer(i -> i.getArguments()[0]);
        when(recordRepository.save(any(Record.class))).thenAnswer(i -> i.getArguments()[0]);

        CalculationRequest request = new CalculationRequest();
        request.setOperationType(OperationType.ADDITION);
        request.setFirstNum(5.0);
        request.setSecondNum(3.0);

        // Act
        Optional<RecordDTO> result = calculationService.performOperation(USER_EMAIL, OperationType.ADDITION, 5.0, 3.0);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(40.0, user.getBalance());
        assertEquals("8.0", result.get().getOperationResponse());

        verify(userRepository, times(1)).save(user);
        verify(operationRepository, times(1)).save(any(Operation.class));
        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    void performOperation_insufficientBalance() {
        // Arrange
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setBalance(5.0);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        // Act
        Optional<RecordDTO> result = calculationService.performOperation(USER_EMAIL, OperationType.ADDITION, 5.0, 3.0);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, never()).save(user);
        verify(operationRepository, never()).save(any(Operation.class));
        verify(recordRepository, never()).save(any(Record.class));
    }

    @Test
    void performOperation_userNotFound() {
        // Arrange
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        // Act
        Optional<RecordDTO> result = calculationService.performOperation(USER_EMAIL, OperationType.ADDITION, 5.0, 3.0);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, never()).save(any(User.class));
        verify(operationRepository, never()).save(any(Operation.class));
        verify(recordRepository, never()).save(any(Record.class));
    }

    @Test
    void performOperation_divisionByZero() {
        // Arrange
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setBalance(50.0);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                calculationService.performOperation(USER_EMAIL, OperationType.DIVISION, 5.0, 0.0));
    }

    @Test
    void performOperation_squareRootOfNegativeNumber() {
        // Arrange
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setBalance(50.0);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                calculationService.performOperation(USER_EMAIL, OperationType.SQUARE_ROOT, -5.0, null));
    }

    @Test
    void performOperation_randomString_success() {
        // Arrange
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setBalance(50.0);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(operationRepository.save(any(Operation.class))).thenAnswer(i -> i.getArguments()[0]);
        when(recordRepository.save(any(Record.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Optional<RecordDTO> result = calculationService.performOperation(USER_EMAIL, OperationType.RANDOM_STRING, null, null);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(40.0, user.getBalance());
        assertNotNull(result.get().getOperationResponse());
        assertEquals(10, result.get().getOperationResponse().length());

        verify(userRepository, times(1)).save(user);
        verify(operationRepository, times(1)).save(any(Operation.class));
        verify(recordRepository, times(1)).save(any(Record.class));
    }
}
