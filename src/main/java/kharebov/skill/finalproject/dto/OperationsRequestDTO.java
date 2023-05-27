package kharebov.skill.finalproject.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OperationsRequestDTO {
    @NotNull(message = "id can not be empty")
    private Long id;

    @PastOrPresent(message = "date can not be in future")
    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
