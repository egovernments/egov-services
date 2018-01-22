package org.egov.pa.repository;

import java.util.List;

import org.egov.pa.model.KpiTarget;
import org.egov.pa.web.contract.KPITargetGetRequest;
import org.egov.pa.web.contract.KPITargetRequest;

public interface KpiTargetRepository {
	
	public void persistNewTarget(KPITargetRequest kpiTargetRequest); 
	
	public void modifyNewTarget(KPITargetRequest kpiTargetRequest); 
	
	public List<KpiTarget> searchKpiTargets(KPITargetGetRequest getReq); 

	public boolean checkActualValuesForKpi(List<String> kpiCodeList); 
}
