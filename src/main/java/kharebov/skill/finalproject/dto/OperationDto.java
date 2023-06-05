package kharebov.skill.finalproject.dto;

import kharebov.skill.finalproject.utils.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationDto {

    private LocalDateTime time;

    private OperationType type;

    private Double amount;

    @Override
    public String toString() {
        return
                time.toLocalDate() +
                "/ тип : " + type +
                ", сумма : " + amount +
                ".";
    }
}
