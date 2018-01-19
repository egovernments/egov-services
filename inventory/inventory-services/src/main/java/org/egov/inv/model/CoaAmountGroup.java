package org.egov.inv.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoaAmountGroup {

    private String chartofAccount;

    private BigDecimal amount;
}
