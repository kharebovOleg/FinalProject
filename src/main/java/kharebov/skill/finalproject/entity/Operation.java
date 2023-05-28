package kharebov.skill.finalproject.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import kharebov.skill.finalproject.util.enums.OperationType;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
@Data
public class Operation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    @NotNull(message = "amount can not be null")
    @Positive(message = "amount should be positive")
    private Double amount;

    @Column(name = "type")
    @NotNull(message = "type should not be null")
    @Enumerated(EnumType.STRING)
    private OperationType type;

    @Column(name = "time")
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
