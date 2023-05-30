package kharebov.skill.finalproject.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Данные для вывода списка операций")
public class OperationsRequestDTO {
    @NotNull(message = "id can not be empty")
    @Schema(description = "id клиента")
    private Long id;

    @PastOrPresent(message = "date can not be in future")
    @Schema(description = "с какой даты показать операции")
    private LocalDate startDate;

    @Schema(description = "до какой даты показать операции")
    private LocalDate endDate;

}
