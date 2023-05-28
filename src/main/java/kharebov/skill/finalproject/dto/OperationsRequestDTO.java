package kharebov.skill.finalproject.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationsRequestDTO {
    @NotNull(message = "id can not be empty")
    private Long id;

    @PastOrPresent(message = "date can not be in future")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

}
