package org.egov.works.estimate.web.contract;

import lombok.Getter;
import lombok.Setter;
import org.egov.works.estimate.web.model.RequestInfo;

import java.util.List;

@Getter
@Setter
public class IdGenerationRequest {

    public RequestInfo requestInfo;

    private List<IdRequest> idRequests;
}
