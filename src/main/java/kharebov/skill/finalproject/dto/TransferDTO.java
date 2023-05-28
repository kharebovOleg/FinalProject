package kharebov.skill.finalproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "данные для перевода средств")
public class TransferDTO {

    @NotNull(message = "sender_id can not be empty")
    @Schema(description = "id отправителя")
    private Long senderId;

    @NotNull(message = "recipient_id can not be empty")
    @Schema(description = "id получателя")
    private Long recipientId;

    @NotNull(message = "amount can not be empty")
    @Positive(message = "amount should be positive")
    @Schema(description = "сумма")
    private Double amount;
}
