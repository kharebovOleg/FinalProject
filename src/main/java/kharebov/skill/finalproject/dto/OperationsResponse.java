package kharebov.skill.finalproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Выводимый список операций")
public class OperationsResponse {

    @Schema(description = """
            Список из строк со следующими полями:

            Время операции
            Тип операции\s
            Сумма операции""")
    private List<String> operations;

}
