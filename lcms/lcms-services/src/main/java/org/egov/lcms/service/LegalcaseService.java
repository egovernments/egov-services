package org.egov.lcms.service;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.LegalCaseRequest;
import org.egov.lcms.models.LegalCaseResponse;
import org.egov.lcms.models.LegalCaseSearchCriteria;
import org.egov.lcms.models.LegalCaseSearchResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegalcaseService {


	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	PropertiesManager propertiesManager;

	public LegalCaseResponse createLegalCase(LegalCaseRequest legalcaseReuqest) throws Exception {
		insertLegalCase(legalcaseReuqest);
		return null;
	}
	
	public LegalCaseResponse updateLegalCase(LegalCaseRequest legalcaseReuqest) throws Exception {
		return null;
	}
	
	public LegalCaseSearchResponse searchLegalCase(LegalCaseSearchCriteria legalCaseSearchCriteria) throws Exception{
		return null;
	}
	 
	
	private void insertLegalCase(LegalCaseRequest legalCaseRequest) throws Exception{
		
		kafkaTemplate.send(propertiesManager.getCreateLegalCase(),
				legalCaseRequest);
		
	}
	
	public LegalCaseResponse getLegalCaseReponse(LegalCaseRequest legalCaseRequest){
		return null;
	}

}
