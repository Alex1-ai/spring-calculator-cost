package com.chidi.moneycalculator.record;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    public List<Record> getRecordsByUserId(Integer userId) {
        return recordRepository.findByUserId(userId);
    }

    public Record saveRecord(Record record) {
        return recordRepository.save(record);
    }

    public void deleteRecord(Integer id) {
        recordRepository.deleteById(id);
    }
}

