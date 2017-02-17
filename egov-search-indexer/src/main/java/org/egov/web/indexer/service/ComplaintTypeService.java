package org.egov.web.indexer.service;

import org.egov.web.indexer.contract.ComplaintType;

public interface ComplaintTypeService {
	ComplaintType fetchComplaintTypeByCode(String code);

}