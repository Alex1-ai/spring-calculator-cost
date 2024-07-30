package com.chidi.moneycalculator.operation;


import com.chidi.moneycalculator.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {
    Optional<Operation> findByType(OperationType type);
}
