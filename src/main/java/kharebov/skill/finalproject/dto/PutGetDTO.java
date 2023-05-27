package kharebov.skill.finalproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutGetDTO {
    @NotNull(message = "id can not be empty")
    private Long id;

    @NotNull(message = "amount can not be empty")
    @Positive(message = "amount should be positive")
    private Double amount;
}
