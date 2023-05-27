package kharebov.skill.finalproject.services;

import kharebov.skill.finalproject.entity.Operation;
import kharebov.skill.finalproject.entity.User;
import kharebov.skill.finalproject.repositories.UserRepository;
import kharebov.skill.finalproject.util.exceptions.NotEnoughMoneyException;
import kharebov.skill.finalproject.util.exceptions.SameIdException;
import kharebov.skill.finalproject.util.exceptions.UserNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class UserBalanceService {
    private final UserRepository userRepository;

    public User findUserById(Long id){
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public Double getMoney(Long id) {
        return findUserById(id).getBalance();
    }

    public void putMoney(Long id, Double money) {
        User user = findUserById(id);
        user.setBalance(user.getBalance() + money);
        userRepository.save(user);
    }

    public void takeMoney(Long id, Double money) {
        User user = findUserById(id);
        if (user.getBalance() < money)
            throw new NotEnoughMoneyException("Not enough money");
        else {
            user.setBalance(user.getBalance() - money);
            userRepository.save(user);
        }
    }

    public void transferMoney(Long senderId, Long recipientId, Double amount) {
        if (Objects.equals(senderId, recipientId))
            throw new SameIdException("the sender's id and the recipient's id are equal");
        takeMoney(senderId, amount);
        putMoney(recipientId, amount);
    }

    public List<Operation> getOperationList(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null){
            startDate = LocalDateTime.MIN;
        }
        if (endDate == null) {
            endDate = LocalDateTime.MAX;
        }

        User user = findUserById(id);
        LocalDateTime finalStartDate = startDate;
        LocalDateTime finalEndDate = endDate;

        return user.getOperations().stream()
                        .filter(operation -> operation.getTime().isAfter(finalStartDate)
                                && operation.getTime().isBefore(finalEndDate))
                        .collect(Collectors.toList());
    }


    // TODO: 23.05.2023 реализовать исключения с выводом ошибки в ответе
}
