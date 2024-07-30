package com.chidi.moneycalculator.record;


import com.chidi.moneycalculator.record.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    List<Record> findByUserId(Integer userId);
}
