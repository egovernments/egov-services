package org.egov.pgr.rest.validate;

import org.egov.pgr.model.SevaRequest;
import org.springframework.stereotype.Service;

@Service
public interface ValidateSevaRequest {
	public Boolean validate(SevaRequest request);

}