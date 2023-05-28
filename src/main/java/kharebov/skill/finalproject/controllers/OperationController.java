package kharebov.skill.finalproject.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import kharebov.skill.finalproject.dto.OperationsRequestDTO;
import kharebov.skill.finalproject.dto.OperationsResponseDTO;
import kharebov.skill.finalproject.dto.PutGetDTO;
import kharebov.skill.finalproject.dto.TransferDTO;
import kharebov.skill.finalproject.entity.Operation;
import kharebov.skill.finalproject.util.enums.OperationType;
import kharebov.skill.finalproject.services.OperationService;
import kharebov.skill.finalproject.services.UserBalanceService;
import kharebov.skill.finalproject.util.exceptions.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Data
@Slf4j
@Api("Контроллер для осуществления операций")
public class OperationController {
    private final UserBalanceService userService;
    private final OperationService operationService;
    private final ModelMapper modelMapper;

    @GetMapping("/balance/{id}")
    @ApiOperation("Узнать баланс по ID пользователя")
    public Double getBalance(@PathVariable Long id) {
        return userService.getMoney(id);
    }

    @PostMapping("/take")
    @ApiOperation("Снятие заданной суммы с баланса пользователя")
    public ResponseEntity<String> takeMoney(@RequestBody @Valid PutGetDTO dto) {

        userService.takeMoney(dto.getId(), dto.getAmount());
        Operation operation = operationService.createOperation(dto.getId(), dto.getAmount(), OperationType.TAKE);
        operationService.saveOperation(operation);


        return new ResponseEntity<>("an amount has been withdrawn from your balance", HttpStatus.OK);
    }

    @PostMapping("/put")
    @ApiOperation("Пополнение баланса на заданную сумму")
    public ResponseEntity<String> putMoney(@RequestBody @Valid PutGetDTO dto) {

        userService.putMoney(dto.getId(), dto.getAmount());
        Operation operation = operationService.createOperation(dto.getId(), dto.getAmount(), OperationType.PUT);
        operationService.saveOperation(operation);

        return new ResponseEntity<>(
                "your balance has been replenished by the amount of "
                        + dto.getAmount() + ".",HttpStatus.OK);
    }

    @PostMapping("/transfer")
    @ApiOperation("Перевести заданную сумму другому пользователю")
    public ResponseEntity<String> transferMoney(@RequestBody @Valid TransferDTO dto) {

        userService.transferMoney(dto.getSenderId(), dto.getRecipientId(), dto.getAmount());

        Operation transferTo = operationService.createOperation(dto.getSenderId(),
                dto.getAmount(), OperationType.TRANSFERTO);
        operationService.saveOperation(transferTo);

        Operation transferFrom = operationService.createOperation(dto.getRecipientId(),
                dto.getAmount(), OperationType.TRANSFERFROM);
        operationService.saveOperation(transferFrom);

        return new ResponseEntity<>("amount transferred",HttpStatus.OK);
    }

    @PostMapping("/operations")
    @ApiOperation("Отобразить список операций за выбранный период")
    public ResponseEntity<List<String>> getOperationList(@RequestBody @Valid OperationsRequestDTO dto) {

        if (dto.getStartDate().isAfter(dto.getEndDate())) throw new DataMistakeException(
                "end_date should be later then start_date");

        List<String> operations = new ArrayList<>();
        for (OperationsResponseDTO operationsResponseDTO : convertToOperationResponseDTO(userService.getOperationList(dto.getId(),
                dto.getStartDate(), dto.getEndDate()))) {
            String toString = operationsResponseDTO.toString();
            operations.add(toString);
        }
        return new ResponseEntity<>(operations, HttpStatus.OK);
    }


    private List<OperationsResponseDTO> convertToOperationResponseDTO(List<Operation> operations) {

        List<OperationsResponseDTO> operationsInfo = new ArrayList<>();
        for (Operation o :
                operations) {
            operationsInfo.add(modelMapper.map(o, OperationsResponseDTO.class));
        }
        return operationsInfo;
    }

}
