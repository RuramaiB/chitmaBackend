package me.ruramaibotso.umc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ruramaibotso.umc.user.Organisation;
import me.ruramaibotso.umc.user.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SectionFinance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer financeID;
    @ManyToOne
    private FinanceDescription financeDescription;
    private Double amount;
    @DateTimeFormat(pattern = "dd/MMM/yyyy h:m")
    private LocalDateTime dateOfPayment;
    private String currency;
    private String paymentMethod;
    private String membershipNumber;
    private String phoneNumber;
    @ManyToOne
    private User user;
    @ManyToOne
    private Section section;
    @ManyToOne
    private Locals locals;
}
