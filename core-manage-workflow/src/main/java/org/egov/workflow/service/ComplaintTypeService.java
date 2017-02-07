package org.egov.workflow.service;

import org.egov.workflow.model.ComplaintTypeResponse;

public interface ComplaintTypeService {

	ComplaintTypeResponse fetchComplaintTypeByCode(String code);

}
