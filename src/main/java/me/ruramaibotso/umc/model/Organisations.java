package me.ruramaibotso.umc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ruramaibotso.umc.user.Organisation;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Organisations {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Organisation organisation;
    @ManyToOne(fetch = FetchType.EAGER)
    private Locals locals;
    @OneToOne
    private OrganisationLeadership organisationLeadership;
}
