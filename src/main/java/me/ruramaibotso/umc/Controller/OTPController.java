package me.ruramaibotso.umc.Controller;

import com.twilio.Twilio;
import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.Response.PasswordResetResponse;
import me.ruramaibotso.umc.requests.OTPRequest;
import me.ruramaibotso.umc.services.OTPService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OTPController {

    private final OTPService otpService;

    @PostMapping("/sendOTP")
    public PasswordResetResponse sendOTP(@RequestBody OTPRequest otpRequest){

        return otpService.sendOTPForPasswordReset(otpRequest);
    }
    @PostMapping("/validateOTP/{membershipNumber}/{otp}")
    public Boolean validateOTP(@PathVariable String otp, @PathVariable String membershipNumber){
        return otpService.validateOTP(otp, membershipNumber);
    }
}
