package org.egov.pgr.rest.web.location.assign;

import org.egov.pgr.rest.web.model.ServiceRequest;
import org.springframework.stereotype.Service;

@Service
public interface AssignSevaRequestLocation {
	public Boolean assign(ServiceRequest request);

}