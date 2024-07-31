package com.chidi.moneycalculator.record;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public List<RecordDTO> getRecordsByUserEmail(String userEmail) {
        List<Record> records = recordRepository.findByUserEmail(userEmail);
        return records.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public RecordDTO saveRecord(Record record) {
        Record savedRecord = recordRepository.save(record);
        return mapToDTO(savedRecord);
    }

    public void deleteRecordByUserEmail(Integer recordId, String userEmail) {
        recordRepository.findById(recordId).ifPresent(record -> {
            if (record.getUser().getEmail().equals(userEmail)) {
                recordRepository.delete(record);
            }
        });
    }

    private RecordDTO mapToDTO(Record record) {
        return RecordDTO.builder()
                .id(record.getId())
                .operation(record.getOperation())
                .userId(record.getUser().getId())
                .email(record.getUser().getEmail())
                .firstName(record.getUser().getFirstname())
                .amount(record.getAmount())
                .userBalance(record.getUserBalance())
                .operationResponse(record.getOperationResponse())
                .timestamp(record.getTimestamp())
                .build();
    }
}
