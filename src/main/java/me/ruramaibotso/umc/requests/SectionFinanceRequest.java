package me.ruramaibotso.umc.requests;

import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.ruramaibotso.umc.model.Section;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class SectionFinanceRequest {
    private String Description;
    private Double amount;
    private String paymentMethod;
    private String section;
    private String membershipNumber;
    private String currency;
    private String local;
    private String phoneNumber;
}
