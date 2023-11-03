package me.ruramaibotso.umc.requests;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OTPRequest {
    private String membershipNumber;
    private String phoneNumber;
}
