package kharebov.skill.finalproject.services;

import kharebov.skill.finalproject.entity.Operation;
import kharebov.skill.finalproject.repositories.OperationsRepository;
import kharebov.skill.finalproject.util.enums.OperationType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Data
@RequiredArgsConstructor
public class OperationService {
    private final OperationsRepository operationsRepository;
    private final UserBalanceService userService;

    public void saveOperation(Operation operation){
        operation.setTime(LocalDateTime.now());
        operationsRepository.save(operation);
    }

    public Operation createOperation(Long id, Double amount, OperationType type){
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setOwner(userService.findUserById(id));
        operation.setType(type);

        return operation;
    }

}
