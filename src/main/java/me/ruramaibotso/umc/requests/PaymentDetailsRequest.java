package me.ruramaibotso.umc.requests;

import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import me.ruramaibotso.umc.model.Locals;

@Data
@ToString
public class PaymentDetailsRequest {

    private String paymentMethod;
    private String paymentId;
    private String paymentKey;
    private String currency;
    private String local;
}
