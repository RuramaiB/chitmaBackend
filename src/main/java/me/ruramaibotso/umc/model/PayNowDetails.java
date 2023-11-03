package me.ruramaibotso.umc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ruramaibotso.umc.user.User;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PayNowDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer paymentID;
    private LocalDate dateOfPayment;
    private LocalTime timeOfPayment;
    private String email;
    private String phoneNumber;
    private Double amount;
    private String membershipNumber;
    private String method;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @ManyToOne
    private FinanceDescription financeDescription;
    @ManyToOne
    private User user;
}
