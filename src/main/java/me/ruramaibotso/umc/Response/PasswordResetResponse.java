package me.ruramaibotso.umc.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ruramaibotso.umc.model.OtpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetResponse {
    private OtpStatus status;
    private String message;
}
