package me.ruramaibotso.umc.auth;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ruramaibotso.umc.user.Gender;
import me.ruramaibotso.umc.user.Membership;
import me.ruramaibotso.umc.user.Organisation;
import me.ruramaibotso.umc.user.Role;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterRequest {

    private String firstname;
    private String lastname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate dateOfBirth;
    private String password;
    private String local;

}