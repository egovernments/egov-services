package org.egov.pgr.employee.enrichment.repository.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ComplaintTypeResponse {
    private Long id;
    private String serviceName;
    private String serviceCode;
    private String tenantId;
}
