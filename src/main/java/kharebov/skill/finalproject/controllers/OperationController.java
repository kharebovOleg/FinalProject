package kharebov.skill.finalproject.controllers;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
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
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Data
@Slf4j
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
    public ResponseEntity<HttpStatus> takeMoney(@RequestBody @Valid PutGetDTO dto,
                                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            throw new PutGetDtoNotCreatedException(createMsg(bindingResult).toString());
        }

        Operation operation = operationService.createOperation(dto.getId(), dto.getAmount(), OperationType.TAKE);
        operationService.saveOperation(operation);
        userService.takeMoney(dto.getId(), dto.getAmount());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/put")
    @ApiOperation("Пополнение баланса на заданную сумму")
    public ResponseEntity<HttpStatus> putMoney(@RequestBody @Valid PutGetDTO dto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new PutGetDtoNotCreatedException(createMsg(bindingResult).toString());
        }

        Operation operation = operationService.createOperation(dto.getId(), dto.getAmount(), OperationType.PUT);
        operationService.saveOperation(operation);
        userService.putMoney(dto.getId(), dto.getAmount());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/transfer")
    @ApiOperation("Перевести заданную сумму другому пользователю")
    public ResponseEntity<HttpStatus> transferMoney(@RequestBody @Valid TransferDTO dto,
                                                    BindingResult bindingResult) {


        // TODO: 27.05.2023 метод при выбросе исключения все равно продолжает работать!

        // TODO: 27.05.2023 подумать над реализацией валидатора для создания операции,
        //  который заменит отдельно созданные исключения.




        if (!bindingResult.hasErrors()){
            Operation transferTo = operationService.createOperation(dto.getSenderId(),
                    dto.getAmount(), OperationType.TRANSFERTO);
            operationService.saveOperation(transferTo);

            Operation transferFrom = operationService.createOperation(dto.getRecipientId(),
                    dto.getAmount(), OperationType.TRANSFERFROM);
            operationService.saveOperation(transferFrom);

            userService.transferMoney(dto.getSenderId(), dto.getRecipientId(), dto.getAmount());
        }
        else {
            throw new TransferDtoNotCreatedException(createMsg(bindingResult).toString());
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/operations")
    @ApiOperation("Отобразить список операций за выбранный период")
    public List<String> getOperationList(@RequestBody @Valid OperationsRequestDTO dto,
                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        return convertToOperationResponseDTO(userService.getOperationList(dto.getId(),
                dto.getStartDate(), dto.getEndDate()))
                .stream()
                .map(OperationsResponseDTO::toString).collect(Collectors.toList());
    }


    private List<OperationsResponseDTO> convertToOperationResponseDTO(List<Operation> operations) {

        List<OperationsResponseDTO> operationsInfo = new ArrayList<>();
        for (Operation o :
                operations) {
            operationsInfo.add(modelMapper.map(o, OperationsResponseDTO.class));
        }
        System.out.println(operationsInfo);
        return operationsInfo;
    }
    private StringBuilder createMsg(BindingResult b) {
        StringBuilder errorMsg = new StringBuilder();

        List<FieldError> errors = b.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage())
                    .append(";");
        }
        return errorMsg;
    }


    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse(
                "user with this id was not found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(TransferDtoNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(PutGetDtoNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(NotEnoughMoneyException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(SameIdException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(HttpMessageNotReadableException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
