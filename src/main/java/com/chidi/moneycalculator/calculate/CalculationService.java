package com.chidi.moneycalculator.calculate;

import com.chidi.moneycalculator.operation.Operation;
import com.chidi.moneycalculator.operation.OperationType;
import com.chidi.moneycalculator.record.Record;
import com.chidi.moneycalculator.record.RecordDTO;
import com.chidi.moneycalculator.user.User;
import com.chidi.moneycalculator.operation.OperationRepository;
import com.chidi.moneycalculator.record.RecordRepository;
import com.chidi.moneycalculator.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final OperationRepository operationRepository;
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final RandomStringService randomStringService;
    private static final double FIXED_COST = 10.0;

    public Optional<RecordDTO> performOperation(String userEmail, OperationType operationType, Double firstNum, Double secondNum) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();
        if (user.getBalance() < FIXED_COST) {
            return Optional.empty(); // Insufficient balance
        }

        String operationResponse = calculate(operationType, firstNum, secondNum);

        Operation operation = Operation.builder()
                .cost(FIXED_COST)
                .type(operationType)
                .build();
        operationRepository.save(operation);
        user.setBalance(user.getBalance() - operation.getCost());
        userRepository.save(user);

        Record record = Record.builder()
                .operation(operation)
                .user(user)
                .amount(FIXED_COST)
                .userBalance(user.getBalance())
                .operationResponse(operationResponse)
                .build();
        recordRepository.save(record);

        return Optional.of(mapToDTO(record));
    }

    private String calculate(OperationType operationType, Double firstNum, Double secondNum) {
        return switch (operationType) {
            case ADDITION -> String.valueOf(firstNum + secondNum);
            case SUBTRACTION -> String.valueOf(firstNum - secondNum);
            case MULTIPLICATION -> String.valueOf(firstNum * secondNum);
            case DIVISION -> {
                if (secondNum == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
                yield String.valueOf(firstNum / secondNum);
            }
            case SQUARE_ROOT -> {
                if (firstNum < 0) {
                    throw new IllegalArgumentException("Square root of negative number is not allowed.");
                }
                yield String.valueOf(Math.sqrt(firstNum));
            }
            case RANDOM_STRING -> randomStringService.getRandomString();
            default -> throw new IllegalArgumentException("Invalid operation type.");
        };
    }

    private RecordDTO mapToDTO(Record record) {
        return RecordDTO.builder()
                .id(record.getId())
                .operation(record.getOperation())
                .userId(record.getUser().getId())
                .firstName(record.getUser().getFirstname())
                .email(record.getUser().getEmail())
                .amount(record.getAmount())
                .userBalance(record.getUserBalance())
                .operationResponse(record.getOperationResponse())
                .timestamp(record.getTimestamp())
                .build();
    }
}
