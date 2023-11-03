package me.ruramaibotso.umc.requests;

import jakarta.annotation.Nullable;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.user.User;

import java.time.LocalDateTime;

@Data
@ToString
public class PledgesRequest {
    private String Description;
    private Double amount;
    private String paymentMethod;
    @Nullable
    private String membershipNumber;
    private LocalDateTime dateOfPayment;
    private String phoneNumber;
    private String currency;
    private String locals;
}
