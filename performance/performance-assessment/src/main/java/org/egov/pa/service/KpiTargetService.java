package org.egov.pa.service;

import org.egov.pa.web.contract.KPITargetRequest;
import org.springframework.stereotype.Service;

@Service
public interface KpiTargetService {

	public KPITargetRequest createNewTarget(KPITargetRequest kpiTargetRequest); 
	
	public KPITargetRequest updateNewTarget(KPITargetRequest kpiTargetRequest); 

	
	
}
