package kharebov.skill.finalproject.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kharebov.skill.finalproject.dto.*;
import kharebov.skill.finalproject.entity.Operation;
import kharebov.skill.finalproject.utils.enums.OperationType;
import kharebov.skill.finalproject.services.UserBalanceService;
import kharebov.skill.finalproject.utils.exceptions.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Data
@Slf4j
@Tag(name = "Operation controller", description = "контроллер для выполнения финансовых операций")
public class OperationController {
    private final UserBalanceService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/balance/{id}")
    @io.swagger.v3.oas.annotations.Operation(
            summary = "проверка баланса",
            description = "показывает баланс клиента"
    )
    public Double getBalance(@PathVariable @Parameter(description = "id клиента") Long id) {
        return userService.getMoney(id);
    }

    @PostMapping("/take")
    @io.swagger.v3.oas.annotations.Operation(
            summary = "снятие средств",
            description = "списывает указанную в теле запроса сумму с указанного клиента"
    )
    public ResponseEntity<String> takeMoney(@RequestBody @Valid PutGetDTO dto) {

        userService.takeMoney(dto.getId(), dto.getAmount(), OperationType.TAKE);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/put")
    @io.swagger.v3.oas.annotations.Operation(
            summary = "пополнение баланса",
            description = "пополнить баланс указанного в запросе клиента на указанную сумму"
    )
    public ResponseEntity<String> putMoney(@RequestBody @Valid PutGetDTO dto) {

        userService.putMoney(dto.getId(), dto.getAmount(), OperationType.PUT);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/transfer")
    @io.swagger.v3.oas.annotations.Operation(
            summary = "перевод средств",
            description = "переводит средства со счета другому клиенту"
    )
    public ResponseEntity<String> transferMoney(@RequestBody @Valid TransferDTO dto) {

        userService.transferMoney(dto.getSenderId(), dto.getRecipientId(), dto.getAmount());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/operations")
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Отобразить список операций за выбранный период"
    )
    public ResponseEntity<OperationsResponse> getOperationList(@RequestBody @Valid OperationsRequestDTO dto) {

        LocalDateTime startTime;
        LocalDateTime endTime;

        if (dto.getStartDate() == null){
            startTime = LocalDateTime.MIN;
        } else startTime = dto.getStartDate().atTime(0,0,0);
        if (dto.getEndDate() == null) {
            endTime = LocalDateTime.MAX;
        } else endTime = dto.getEndDate().atTime(23,59,59);

        if (startTime.isAfter(endTime)) throw new DataMistakeException(
                "end_date should be later then start_date");

        List<Operation> operations = userService.getOperationList(dto.getId(), startTime, endTime);
        OperationsResponse response = new OperationsResponse(convertToOperationResponseDTO(operations)
                .stream().map(OperationDto::toString).collect(Collectors.toList()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<OperationDto> convertToOperationResponseDTO(List<Operation> operations) {

        List<OperationDto> operationsInfo = new ArrayList<>();
        for (Operation o :
                operations) {
            operationsInfo.add(modelMapper.map(o, OperationDto.class));
        }
        return operationsInfo;
    }

}
