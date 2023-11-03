package me.ruramaibotso.umc.requests;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class FinanceDescriptionRequest {
    private String description;
    private Long target;
    private LocalDateTime kickOffDate;
    private String local;
}
