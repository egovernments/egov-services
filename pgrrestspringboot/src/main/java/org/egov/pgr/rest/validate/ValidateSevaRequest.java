package org.egov.pgr.rest.validate;

import org.egov.pgr.model.ServiceRequestReq;
import org.springframework.stereotype.Service;

@Service
public interface ValidateSevaRequest {
	public Boolean validate(ServiceRequestReq request);

}