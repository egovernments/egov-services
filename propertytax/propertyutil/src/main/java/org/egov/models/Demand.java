package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demand {
    private String id;

    @NotNull
    private String tenantId;

    @NotNull
    private String consumerCode;

    @NotNull
    private String consumerType;

    @NotNull
    private String businessService;

    @NotNull
    private Owner owner;

    @NotNull
    private Long taxPeriodFrom;

    @NotNull
    private Long taxPeriodTo;

    @Valid
    @NotNull
    private List<DemandDetail> demandDetails = new ArrayList<DemandDetail>();

    private BigDecimal minimumAmountPayable = BigDecimal.ZERO;

    private AuditDetails auditDetail;
}
