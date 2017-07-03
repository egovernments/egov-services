package org.egov.pgrrest.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgrrest.common.contract.web.ServiceRequest;

import java.util.List;

@Getter
@AllArgsConstructor
public class ServiceResponse {

    private ResponseInfo responseInfo;
    private List<ServiceRequest> serviceRequests;
}

