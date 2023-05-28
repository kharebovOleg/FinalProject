package kharebov.skill.finalproject.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Выводимый список операций")
public class OperationsResponseDTO {

    @NotNull(message = "amount can not be empty")
    @Positive(message = "amount should be positive")
    @Schema(description = "сумма")
    private Double amount;

    @NotEmpty(message = "type can not be empty")
    @Schema(description = "тип операции")
    private String type;

    @Schema(description = "дата операции")
    private LocalDateTime time;

    @Override
    public String toString() {
        return
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", time=" + time;
    }
}
