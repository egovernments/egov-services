package org.egov.workflow.domain.service;

import java.util.List;

import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.egov.workflow.domain.model.EscalationTimeType;
import org.egov.workflow.persistence.repository.EscalationRepository;
import org.egov.workflow.web.contract.EscalationTimeTypeGetReq;
import org.egov.workflow.web.contract.EscalationTimeTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EscalationService {
	
	public static final Logger logger = LoggerFactory.getLogger(EscalationService.class);

    private EscalationRepository escalationRepository;

    public EscalationService(EscalationRepository escalationRepository) {
        this.escalationRepository = escalationRepository;
    }

    public int getEscalationHours(EscalationHoursSearchCriteria searchCriteria) {
        return escalationRepository.getEscalationHours(searchCriteria);
    }
    
	public EscalationTimeTypeReq create(EscalationTimeTypeReq escalationTimeTypeReq) {
		logger.info("Persisting escilation time type record");
		EscalationTimeTypeReq response = new EscalationTimeTypeReq();
		try{
			response = escalationRepository.persistCreateEscalationTimeType(escalationTimeTypeReq);
		}catch(Exception e){
			logger.error("Persisiting escilation time type record FAILED.", e.getCause());
			return null;
		}
		
		return response;
	}
	
	public EscalationTimeTypeReq update(EscalationTimeTypeReq escalationTimeTypeReq) {
		logger.info("Updating escilation time type record");
		EscalationTimeTypeReq response = new EscalationTimeTypeReq();
		try{
			response = escalationRepository.persistUpdateEscalationTimeType(escalationTimeTypeReq);
		}catch(Exception e){
			logger.error("Updating escilation time type record FAILED.", e.getMessage());
			return null;
		}
		
		return response;
	}
	public List<EscalationTimeType> getAllEscalationTimeTypes(final EscalationTimeTypeGetReq escalationGetRequest) {
        return escalationRepository.getAllEscalationTimeTypes(escalationGetRequest);
    }
	
	public boolean checkRecordExists (EscalationTimeTypeReq escalationTimeTypeReq) {
		return escalationRepository.checkRecordExists(escalationTimeTypeReq);
	}
}
