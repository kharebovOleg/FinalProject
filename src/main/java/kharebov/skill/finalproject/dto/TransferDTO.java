package kharebov.skill.finalproject.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {

    @NotNull(message = "sender_id can not be empty")
    private Long senderId;

    @NotNull(message = "recipient_id can not be empty")
    private Long recipientId;

    @NotNull(message = "amount can not be empty")
    @Positive(message = "amount should be positive")
    private Double amount;
}
