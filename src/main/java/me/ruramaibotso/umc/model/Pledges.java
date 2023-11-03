package me.ruramaibotso.umc.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ruramaibotso.umc.user.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pledges {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer financeID;
    private String Description;
    private Double amount;
    private String paymentMethod;
    @Nullable
    private String membershipNumber;
    @DateTimeFormat(pattern = "dd/MMM/yyyy h:m")
    private LocalDateTime dateOfPayment;
    private String phoneNumber;
    private String currency;
    @ManyToOne
    @Nullable
    private User user;
    @ManyToOne
    private Locals locals;

}
