package org.egov.eis.web.contract;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BaseRegisterReportRequest {

    private Long employeeStatus;

    private Long employeeType;

    private Long recruitmentType;

    @NotNull
    @Size(min = 1, max = 256)
    private String tenantId;
}
