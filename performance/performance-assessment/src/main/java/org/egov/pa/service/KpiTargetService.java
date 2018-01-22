package org.egov.pa.service;

import java.util.List;

import org.egov.pa.model.KpiTarget;
import org.egov.pa.web.contract.KPITargetGetRequest;
import org.egov.pa.web.contract.KPITargetRequest;
import org.springframework.stereotype.Service;

@Service
public interface KpiTargetService {

	public KPITargetRequest createNewTarget(KPITargetRequest kpiTargetRequest); 
	
	public KPITargetRequest updateNewTarget(KPITargetRequest kpiTargetRequest);
	
	public List<KpiTarget> searchKpiTarget(KPITargetGetRequest getReq);
	
	public boolean checkActualValuesForKpi(KPITargetRequest kpiTargetRequest); 

	
	
}
