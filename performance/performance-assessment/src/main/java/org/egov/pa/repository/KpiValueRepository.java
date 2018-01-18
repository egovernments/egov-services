package org.egov.pa.repository;

import java.util.List;

import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.ULBKpiValueList;
import org.egov.pa.model.ValueDocument;
import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;

public interface KpiValueRepository {
	
	public void persistKpiValue(final KPIValueRequest kpiValueRequest);
    
    public void updateKpiValue(final KPIValueRequest kpiValueRequest);
    
    public void persistKpiValueDetail(final KPIValueRequest kpiValueRequest);

    public List<KpiValue> searchKpiValue(KPIValueSearchRequest kpiValueSearchReq);
    
    public List<ULBKpiValueList> compareSearchKpiValue(KPIValueSearchRequest kpiValueSearchReq);
    
    public List<ULBKpiValueList> compareSearchObjectiveKpiValue(KPIValueSearchRequest kpiValueSearchReq);
    
    public List<KPI> checkKpiExists(String kpiCode); 
    
    public List<KPI> fetchTargetForKpi(String kpiCode, String finYear); 
    
    public Long checkKpiValueExistsForTenant(String kpiCode, String tenantId); 
    
    public String searchPossibilityCheck(String tenantCount, String kpiCount, String finYearCount);
    
    public List<String> getNewKpiIds(int numberOfIds); 
    
    public int numberOfDocsRequired(String kpiCode); 
    
    public List<ValueDocument> getDocsForValueRecords(List<String> valueIdList); 
}
