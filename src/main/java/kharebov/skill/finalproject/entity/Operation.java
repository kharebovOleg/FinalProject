package kharebov.skill.finalproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kharebov.skill.finalproject.utils.enums.OperationType;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Override
    public String toString() {
        return "Operation{" +

                "время=" + time +
                ", тип='" + type + '\'' +
                ", сумма=" + amount +
                '}';
    }
}
