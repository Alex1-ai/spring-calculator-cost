package com.chidi.moneycalculator.record;

import com.chidi.moneycalculator.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<RecordDTO>> getAllRecords(HttpServletRequest request) {
        String userEmail = extractUserEmailFromToken(request);
        return ResponseEntity.ok(recordService.getRecordsByUserEmail(userEmail));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Integer id, HttpServletRequest request) {
        String userEmail = extractUserEmailFromToken(request);
        recordService.deleteRecordByUserEmail(id, userEmail);
        return ResponseEntity.noContent().build();
    }

    private String extractUserEmailFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header is missing or invalid.");
        }
        String token = authHeader.substring(7);
        return jwtService.extractUsername(token);
    }
}
