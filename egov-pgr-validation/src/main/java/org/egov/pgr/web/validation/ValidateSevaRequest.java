package org.egov.pgr.web.validation;

import org.egov.pgr.web.validation.model.ServiceRequestReq;
import org.springframework.stereotype.Service;

@Service
public interface ValidateSevaRequest {
	public Boolean validate(ServiceRequestReq request);

}