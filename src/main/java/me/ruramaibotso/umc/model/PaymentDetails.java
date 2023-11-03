package me.ruramaibotso.umc.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String paymentMethod;
    private String paymentId;
    private String paymentKey;
    private String currency;
    @ManyToOne
    private Locals locals;
}
