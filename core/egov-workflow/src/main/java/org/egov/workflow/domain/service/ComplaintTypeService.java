package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.ComplaintTypeResponse;

public interface ComplaintTypeService {

	ComplaintTypeResponse fetchComplaintTypeByCode(String code);

}
