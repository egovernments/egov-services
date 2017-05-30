package org.egov.mr.service;

import java.util.List;

import org.egov.mr.model.MarriageDocumentType;
import org.egov.mr.repository.MarriageDocumentTypeRepository;
import org.egov.mr.web.contract.MarriageDocumentTypeSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarriageDocumentTypeService {

	@Autowired
	private MarriageDocumentTypeRepository marriageDocumentTypeRepository;

	public List<MarriageDocumentType> search(MarriageDocumentTypeSearchCriteria marriageDocumentTypeSearchCriteria) {
		return marriageDocumentTypeRepository.search(marriageDocumentTypeSearchCriteria);
	}
}
