package me.ruramaibotso.umc.requests;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.ruramaibotso.umc.model.OrganisationLeadership;
import me.ruramaibotso.umc.user.Organisation;

@ToString
@Getter
@Setter
public class OrganisationsRequest {
    @Enumerated(EnumType.STRING)
    private Organisation organisation;
    private String preachingPoint;
}
