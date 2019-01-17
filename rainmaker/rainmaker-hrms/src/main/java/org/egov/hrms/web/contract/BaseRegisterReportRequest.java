package org.egov.hrms.web.contract;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BaseRegisterReportRequest {

    private Long employeeStatus;

    private Long employeeType;

    private Long recruitmentType;

    @Min(1)
    @Max(500)
    private Integer pageSize;

    private Integer pageNumber;

    @NotNull
    @Size(min = 1, max = 256)
    private String tenantId;
}
