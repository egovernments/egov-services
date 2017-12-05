package org.egov.pa.repository;

import java.util.List;

import org.egov.pa.model.DocumentTypeContract;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiTargetList;
import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIRequest;

public interface KpiMasterRepository {
	
	public void persistKpi(final KPIRequest kpiRequest); 
	
	public void updateKpi(final KPIRequest kpiRequest);
    
    public void deleteKpi(final KPIRequest kpiRequest);
    
    public void persistKpiTarget(final KpiTargetList kpiTargetList);
    
    public void updateKpiTarget(final KpiTargetList kpiTargetList);
    
    public List<Long> getNewKpiIds(int numberOfIds); 
    
    public List<KPI> searchKpi(final KPIGetRequest kpiGetRequest); 
    
    public List<KPI> checkNameOrCodeExists(KPIRequest kpiRequest);
    
    public Boolean getKpiType(String kpiCode, String tenantId); 
    
    public List<DocumentTypeContract> getDocumentForKpi(String kpiCode);
    
    public List<KPI> getKpiByCode(List<String> kpiCodeList, List<String> finYearList, Long departmentId); 
    
}
