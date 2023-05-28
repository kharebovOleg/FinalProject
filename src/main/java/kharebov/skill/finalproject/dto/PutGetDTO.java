package kharebov.skill.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
