package me.ruramaibotso.umc.requests;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.ruramaibotso.umc.user.Organisation;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class OrganisationsFinanceRequest {
    private String Description;
    private Double amount;
    private String paymentMethod;
    @Enumerated(EnumType.STRING)
    private Organisation organisation;
    private String membershipNumber;
    private String currency;
    private String local;
    private String phoneNumber;
}
