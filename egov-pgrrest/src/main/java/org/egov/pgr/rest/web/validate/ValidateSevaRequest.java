package org.egov.pgr.rest.web.validate;

import org.egov.pgr.rest.web.model.ServiceRequestReq;
import org.springframework.stereotype.Service;

@Service
public interface ValidateSevaRequest {
	public Boolean validate(ServiceRequestReq request);

}