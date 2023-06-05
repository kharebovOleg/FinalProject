package kharebov.skill.finalproject.services;

import kharebov.skill.finalproject.entity.Operation;
import kharebov.skill.finalproject.entity.User;
import kharebov.skill.finalproject.repositories.OperationsRepository;
import kharebov.skill.finalproject.repositories.UserRepository;
import kharebov.skill.finalproject.utils.enums.OperationType;
import kharebov.skill.finalproject.utils.exceptions.NotEnoughMoneyException;
import kharebov.skill.finalproject.utils.exceptions.SameIdException;
import kharebov.skill.finalproject.utils.exceptions.UserNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class UserBalanceService {
    private final UserRepository userRepository;
    private final OperationsRepository operationsRepository;

    @Transactional(readOnly = true)
    public User findUserById(Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user != null) return user;
        else throw new UserNotFoundException("user with id: " + id + " not found");
    }

    @Transactional(readOnly = true)
    public Double getMoney(Long id) {
        return findUserById(id).getBalance();
    }

    @Transactional
    public void putMoney(Long id, Double money, OperationType type) {
        User user = findUserById(id);
        user.setBalance(user.getBalance() + money);
        userRepository.save(user);
        saveOperation(createOperation(id, money, type));
    }

    @Transactional
    public void takeMoney(Long id, Double money, OperationType type) {
        User user = findUserById(id);
        if (user.getBalance() < money)
            throw new NotEnoughMoneyException("Not enough money");
        else {
            user.setBalance(user.getBalance() - money);
            userRepository.save(user);
            saveOperation(createOperation(id, money, type));
        }
    }

    @Transactional
    public void transferMoney(Long senderId, Long recipientId, Double amount) {
        if (Objects.equals(senderId, recipientId))
            throw new SameIdException(
                    "the sender's id :" + senderId +" and the recipient's id : " + recipientId +" are equal");


        putMoney(recipientId, amount, OperationType.TRANSFERTO);
        takeMoney(senderId, amount, OperationType.TRANSFERFROM);
    }

    @Transactional(readOnly = true)
    public List<Operation> getOperationList(Long id, LocalDateTime startDate, LocalDateTime endDate) {

        User user = findUserById(id);

        return user.getOperations().stream()
                        .filter(operation -> operation.getTime().isAfter(startDate)
                                && operation.getTime().isBefore(endDate))
                        .collect(Collectors.toList());
    }

    public void saveOperation(Operation operation){
        operation.setTime(LocalDateTime.now());
        operationsRepository.save(operation);
    }

    public Operation createOperation(Long id, Double amount, OperationType type){
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setOwner(findUserById(id));
        operation.setType(type);

        return operation;
    }

}
