package com.chidi.moneycalculator.calculate;

import com.chidi.moneycalculator.config.JwtService;
import com.chidi.moneycalculator.record.Record;
import com.chidi.moneycalculator.operation.OperationType;
import com.chidi.moneycalculator.record.RecordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/calculate")
@RequiredArgsConstructor
public class CalculartorController {

    private final CalculationService calculationService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> calculate(
            @RequestBody CalculationRequest calculationRequest,
            HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header is missing or invalid.");
        }

        String token = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(token);

        Optional<RecordDTO> recordOptional = calculationService.performOperation(
                userEmail,
                calculationRequest.getOperationType(),
                calculationRequest.getFirstNum(),
                calculationRequest.getSecondNum()
        );

        if (recordOptional.isPresent()) {
            return ResponseEntity.ok(recordOptional.get());
        } else {
            return ResponseEntity.badRequest().body("Calculation failed. Either user not found or insufficient balance.");
        }
    }
}
