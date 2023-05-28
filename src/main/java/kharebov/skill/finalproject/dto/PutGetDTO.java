package kharebov.skill.finalproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "данные для пополнения или снятия средств с баланса")
public class PutGetDTO {
    @NotNull(message = "id can not be empty")
    @Schema(description = "id клиента")
    private Long id;

    @NotNull(message = "amount can not be empty")
    @Positive(message = "amount should be positive")
    @Schema(description = "сумма")
    private Double amount;
}
