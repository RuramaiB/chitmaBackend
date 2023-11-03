package me.ruramaibotso.umc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialTargets {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private double amount;
    @Enumerated(EnumType.STRING)
    private Level level;
    private String target;
    @ManyToOne
    private FinanceDescription financeDescription;
    @ManyToOne
    private Locals locals;

}
