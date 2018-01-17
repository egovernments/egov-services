package org.egov.lams.service;

import java.util.List;

import org.egov.lams.model.ReservationCategory;
import org.egov.lams.repository.AgreementMasterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AgreementMasterService {
	
	public static final Logger logger = LoggerFactory.getLogger(AgreementMasterService.class);
	
	@Autowired
	private AgreementMasterRepository agreementMasterRepository;
		
	public List<ReservationCategory> getReservationCategories(String tenantId){
		return agreementMasterRepository.getReservationCategoryList(tenantId);
	}
	
	

}
