package org.egov.works.estimate.web.contract;

import lombok.Getter;
import lombok.Setter;
import org.egov.works.estimate.web.model.RequestInfo;

@Getter
@Setter
public class IdGenerationRequest {

    public RequestInfo requestInfo;

    public String idName;

    public String tenantId;

    public String format;
}
