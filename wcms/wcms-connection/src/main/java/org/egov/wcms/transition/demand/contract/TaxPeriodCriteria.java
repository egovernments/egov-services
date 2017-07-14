package org.egov.wcms.transition.demand.contract;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

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

    private Set<String> id;

    private String code;

    private Long fromDate;

    private Long toDate;
}
