package com.chidi.moneycalculator.calculate;

import com.chidi.moneycalculator.operation.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CalculationRequest {
    private OperationType operationType;
    private Double firstNum;
    private Double secondNum;


}
