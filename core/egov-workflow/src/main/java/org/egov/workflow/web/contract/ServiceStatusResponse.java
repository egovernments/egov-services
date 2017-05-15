package org.egov.workflow.web.contract;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ServiceStatusResponse {

    private ResponseInfo responseInfo;
    private List<ComplaintStatus> statuses;

    public ServiceStatusResponse(ResponseInfo responseInfo, List<org.egov.workflow.domain.model.ComplaintStatus> statuses) {
        this.responseInfo = responseInfo;
        this.statuses = statuses.stream()
            .map(ComplaintStatus::new)
            .collect(Collectors.toList());
    }
}
