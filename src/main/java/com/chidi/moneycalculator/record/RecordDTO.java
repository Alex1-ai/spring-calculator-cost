package com.chidi.moneycalculator.record;

import com.chidi.moneycalculator.operation.Operation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDTO {
    private Integer id;
    private Operation operation;
    private Integer userId;
    private String email;
    private String firstName;
    private Double amount;
    private Double userBalance;
    private String operationResponse;
    private LocalDateTime timestamp;
}
