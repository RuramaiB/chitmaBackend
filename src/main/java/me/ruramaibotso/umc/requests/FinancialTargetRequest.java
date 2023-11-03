package me.ruramaibotso.umc.requests;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import me.ruramaibotso.umc.model.FinanceDescription;
import me.ruramaibotso.umc.model.Locals;

import java.util.List;

@ToString
@Data
public class FinancialTargetRequest {
    private String financialDescription;
    private String local;
}
