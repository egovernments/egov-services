package org.egov.egf.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.web.contract.AuditableContract;

import java.math.BigDecimal;

/**
 * Created by ritesh on 27/9/17.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RemittanceDetail extends AuditableContract {

    private String id;

    private Remittance remittance;

    private BigDecimal remittedAmount;

}
