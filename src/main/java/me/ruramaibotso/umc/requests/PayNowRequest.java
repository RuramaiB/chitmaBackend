package me.ruramaibotso.umc.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
public class PayNowRequest {
    private String email;
    private String phoneNumber;
    private Double amount;
    private String membershipNumber;
    private String financialDescription;
    private String method;
    private String local;
    private String currency;

}