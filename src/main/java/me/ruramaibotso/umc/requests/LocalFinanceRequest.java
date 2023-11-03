package me.ruramaibotso.umc.requests;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.ruramaibotso.umc.model.Locals;

import java.time.LocalDate;

@ToString
@Getter
@Setter
public class LocalFinanceRequest {
    private String Description;
    private Double amount;
    private String phoneNumber;
    private String paymentMethod;
    private String membershipNumber;
    private String currency;
    private String locals;
}
