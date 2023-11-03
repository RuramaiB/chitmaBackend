package me.ruramaibotso.umc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ruramaibotso.umc.user.Gender;
import me.ruramaibotso.umc.user.Organisation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrganisationLeadership {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private Organisation organisation;
    private String name;
    private String surname;
    private String dateOfBirth;
    private String membershipNumber;
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Leadership leadership;
    private String address;
    private String phoneNumber;
}
