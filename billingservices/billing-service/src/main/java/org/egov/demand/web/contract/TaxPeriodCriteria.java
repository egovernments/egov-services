package org.egov.demand.web.contract;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TaxPeriodCriteria {

    @NotNull
    private String tenantId;

    @NotNull
    private String service;

    private String id;

    private String code;

    private Long fromDate;

    private Long toDate;
}
