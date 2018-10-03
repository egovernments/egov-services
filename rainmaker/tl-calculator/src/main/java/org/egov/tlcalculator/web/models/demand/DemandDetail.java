package org.egov.tlcalculator.web.models.demand;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.tlcalculator.web.models.AuditDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandDetail {

    private String id;

    private String demandId;

    @NotNull
    private String taxHeadMasterCode;

    @NotNull
    private BigDecimal taxAmount;

    @NotNull
    private BigDecimal collectionAmount = BigDecimal.valueOf(0d);

    private AuditDetails auditDetail;

    private String tenantId;
}