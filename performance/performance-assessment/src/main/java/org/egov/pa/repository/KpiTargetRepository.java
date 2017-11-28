package org.egov.pa.repository;

import org.egov.pa.web.contract.KPITargetRequest;

public interface KpiTargetRepository {
	
	public void persistNewTarget(KPITargetRequest kpiTargetRequest); 
	
	public void modifyNewTarget(KPITargetRequest kpiTargetRequest); 

}
