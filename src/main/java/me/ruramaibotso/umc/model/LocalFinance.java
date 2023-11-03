package me.ruramaibotso.umc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ruramaibotso.umc.user.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LocalFinance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer financeID;
    private Double amount;
    private String paymentMethod;
    private String membershipNumber;
    @DateTimeFormat(pattern = "dd/MMM/yyyy h:m")
    private LocalDateTime dateOfPayment;
    private String phoneNumber;
    @ManyToOne
    FinanceDescription financeDescription;
    @ManyToOne
    private User user;
    private String currency;
    @ManyToOne
    private Locals locals;
}
