package org.egov.pa.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.pa.model.AuditDetails;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.KpiValueList;
import org.egov.pa.repository.KpiValueRepository;
import org.egov.pa.service.KpiValueService;
import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("kpiValueServ")
@Slf4j
public class KpiValueServiceImpl implements KpiValueService {
	
	public static Map<String, KPI> kpiMap = new HashMap<>();
	public static Map<String, KPI> kpiTargetMap = new HashMap<>();
	
	@Autowired 
	@Qualifier("kpiValueRepo")
	private KpiValueRepository kpiValueRepository;

	@Override
	public KPIValueRequest createKpiValue(KPIValueRequest kpiValueRequest) {
		int numberOfIds = kpiValueRequest.getKpiValues().size();  
		log.info("KPI Value Create Request Received at Service Level : " + kpiValueRequest);
		List<Long> kpiValueIds = kpiValueRepository.getNewKpiIds(numberOfIds);
		setCreatedDateAndUpdatedDate(kpiValueRequest);
		if(kpiValueIds.size() == kpiValueRequest.getKpiValues().size()) { 
			for(int i=0 ; i < kpiValueIds.size() ; i++) {
				kpiValueRequest.getKpiValues().get(i).setId(String.valueOf(kpiValueIds.get(i)));
				if(null != kpiValueRequest.getKpiValues().get(i).getDocuments()) { 
					for(int j=0 ; j< kpiValueRequest.getKpiValues().get(i).getDocuments().size() ; j++) { 
						kpiValueRequest.getKpiValues().get(i).getDocuments().get(j).setValueId(kpiValueIds.get(i));
						kpiValueRequest.getKpiValues().get(i).getDocuments().get(j).setKpiCode(kpiValueRequest.getKpiValues().get(i).getKpi().getCode());
						kpiValueRequest.getKpiValues().get(i).getDocuments().get(j).setAuditDetails(kpiValueRequest.getKpiValues().get(i).getAuditDetails()); 
					}
				}
			}
		}
    	kpiValueRepository.persistKpiValue(kpiValueRequest);
    	return kpiValueRequest; 
	}

	@Override
	public KPIValueRequest updateKpiValue(KPIValueRequest kpiValueRequest) {
		log.info("KPI Value Update Request Received at Service Level : " + kpiValueRequest);
    	setCreatedDateAndUpdatedDate(kpiValueRequest);
    	kpiValueRepository.updateKpiValue(kpiValueRequest);
    	return kpiValueRequest; 
	}

	@Override
	public List<KpiValueList> compareSearchKpiValue(KPIValueSearchRequest kpiValueSearchReq) {
		return kpiValueRepository.compareSearchKpiValue(kpiValueSearchReq);
	}
	
	@Override
	public List<KpiValue> searchKpiValue(KPIValueSearchRequest kpiValueSearchReq) {
		return kpiValueRepository.searchKpiValue(kpiValueSearchReq); 
	}
	
	
	private void setCreatedDateAndUpdatedDate(KPIValueRequest kpiValueRequest) {
    	List<KpiValue> kpiValues = kpiValueRequest.getKpiValues();
    	for(KpiValue value : kpiValues) { 
    		AuditDetails audit = new AuditDetails(); 
    		audit.setCreatedTime(new java.util.Date().getTime());
    		audit.setCreatedBy(kpiValueRequest.getRequestInfo().getUserInfo().getId());
    		audit.setLastModifiedTime(new java.util.Date().getTime());
    		audit.setLastModifiedBy(kpiValueRequest.getRequestInfo().getUserInfo().getId());
    		value.setAuditDetails(audit);
    	}
    }
	
	public boolean checkKpiExists(String kpiCode) { 
    	if(kpiMap.containsKey(kpiCode)) { 
    		return true;
    	}
    	List<KPI> kpiList = kpiValueRepository.checkKpiExists(kpiCode); 
    	if(null != kpiList && kpiList.size() > 0) { 
    		for(KPI kpi : kpiList) { 
    			kpiMap.put(kpi.getCode(), kpi); 
    		}
    		return true; 
    	} else { 
    		return false; 
    	}
    }
	
	public boolean checkKpiTargetExists(String kpiCode) {
    	String finYear = getFiscalYear();
    	if(kpiTargetMap.containsKey(kpiCode)) { 
    		return true;
    	}
    	List<KPI> kpiList = kpiValueRepository.fetchTargetForKpi(kpiCode, finYear); 
    	if(null != kpiList && kpiList.size() > 0) { 
    		for(KPI kpi : kpiList) 
    			kpiTargetMap.put(kpi.getCode(), kpi); 
    		return true; 
    	} else { 
    		return false; 
    	} 
    }
	
	public boolean checkKpiValueExistsForTenant(String kpiCode, String tenantId) {
    	KpiValue value = kpiValueRepository.checkKpiValueExistsForTenant(kpiCode, tenantId); 
    	if(null != value && StringUtils.isNotBlank(value.getKpi().getCode()) && StringUtils.isNotBlank(value.getTenantId()))  
    		return true;
    	else
    		return false; 
    }
	
	public String searchPossibilityCheck(String tenantCount, String kpiCount, String finYearCount) {
		return kpiValueRepository.searchPossibilityCheck(tenantCount, kpiCount, finYearCount);
	}
	
	private String getFiscalYear() {
        Calendar calendarDate;
        final int FIRST_FISCAL_MONTH = Calendar.MARCH;
        calendarDate = Calendar.getInstance();
        final int month = calendarDate.get(Calendar.MONTH);
        final int year = calendarDate.get(Calendar.YEAR);
        final int value = month >= FIRST_FISCAL_MONTH ? year : year - 1;
        final String finYear = Integer.toString(value) + "-" + Integer.toString(value + 1).substring(2, 4);
        return finYear;
    }
}
