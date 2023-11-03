package me.ruramaibotso.umc.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import me.ruramaibotso.umc.Response.PasswordResetResponse;
import me.ruramaibotso.umc.model.OtpStatus;
import me.ruramaibotso.umc.requests.OTPRequest;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPService {
    Map<String, String> otpMap = new HashMap<>();
    public PasswordResetResponse sendOTPForPasswordReset(OTPRequest otpRequest){
        PasswordResetResponse passwordResetResponse = null;
        Twilio.init("ACe49cf80fdfced68588a44af611a104b6", "ef92081902b11e97c628ca35cf34515c");
        try {
            PhoneNumber to = new PhoneNumber(otpRequest.getPhoneNumber());
            PhoneNumber from = new PhoneNumber("+13367739966");
            String otp = generateOTP();
            String otpMessage = "Dear user, Your OTP is " + otp +" for your password reset.";
            Message message = Message.creator(to, from, otpMessage)
                    .create();
            otpMap.put(otpRequest.getMembershipNumber(), otp);
            passwordResetResponse = new PasswordResetResponse(OtpStatus.Delivered, otpMessage);
        } catch (Exception e){
            passwordResetResponse = new PasswordResetResponse(OtpStatus.Failed, "OTP Request failed.");
        }
        return  passwordResetResponse;
    }

    private String generateOTP(){
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }
    public Boolean validateOTP(String userInputOTP, String membershipNumber){
        if (userInputOTP.equals(otpMap.get(membershipNumber))) {
            return true;
        }
        else {
            return false;
        }
    }

}
