package org.egov.egf.voucher.web.contract ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetSearchContract extends BudgetContract { private Integer pageSize; 
private Integer offset; 
} 