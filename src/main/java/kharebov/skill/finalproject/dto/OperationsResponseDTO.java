package kharebov.skill.finalproject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationsResponseDTO {

    @NotNull(message = "amount can not be empty")
    @Positive(message = "amount should be positive")
    private Double amount;

    @NotEmpty(message = "type can not be empty")
    private String type;

    private LocalDateTime time;

    @Override
    public String toString() {
        return
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", time=" + time;
    }
}
