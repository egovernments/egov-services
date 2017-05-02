package org.egov.pgr.employee.enrichment.repository.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ComplaintResponse {

    private ResponseInfo responseInfo;

    private List<ServiceRequest> serviceRequests;
}
