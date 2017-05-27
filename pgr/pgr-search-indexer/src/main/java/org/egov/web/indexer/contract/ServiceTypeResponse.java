package org.egov.web.indexer.contract;

import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Getter
public class ServiceTypeResponse {

    private ResponseInfo responseInfo;
    private List<ServiceType> complaintTypes;

}