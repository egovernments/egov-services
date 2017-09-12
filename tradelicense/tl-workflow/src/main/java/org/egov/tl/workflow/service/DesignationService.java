package org.egov.tl.workflow.service;

import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.workflow.repository.DesignationRepository;
import org.egov.tl.workflow.repository.contract.Designation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DesignationService {

	private DesignationRepository designationRepository;

	@Autowired
	public DesignationService(DesignationRepository designationRepository) {
		this.designationRepository = designationRepository;
	}

	public List<Designation> getByName(final String designationName, final String tenantId,final RequestInfo requestInfo) {

		return designationRepository.getDesignationByName(designationName, tenantId,requestInfo ).getDesignation();
	}

}
