package org.pgr.batch.repository.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Getter
@AllArgsConstructor
public class ServiceResponse {

    private ResponseInfo responseInfo;

    private List<ServiceRequest> serviceRequests;
}