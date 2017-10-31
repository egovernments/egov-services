package org.egov.pa.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiTarget;
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
	public static Map<String, KpiTarget> kpiTargetMap = new HashMap<>();
	
	@Autowired 
	@Qualifier("kpiValueRepo")
	private KpiValueRepository kpiValueRepository;

	@Override
	public KPIValueRequest createKpiValue(KPIValueRequest kpiValueRequest) {
		log.info("KPI Value Create Request Received at Service Level : " + kpiValueRequest);
    	setCreatedDateAndUpdatedDate(kpiValueRequest);
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
	public List<KpiValueList> searchKpiValue(KPIValueSearchRequest kpiValueSearchReq) {
		return kpiValueRepository.searchKpiValue(kpiValueSearchReq);
	}
	
	
	private void setCreatedDateAndUpdatedDate(KPIValueRequest kpiValueRequest) {
    	List<KpiValue> kpiValues = kpiValueRequest.getKpiValue();
    	for(KpiValue value : kpiValues) { 
    		value.setCreatedDate(new java.util.Date().getTime());
    		value.setCreatedBy(kpiValueRequest.getRequestInfo().getUserInfo().getId());
    		value.setLastModifiedDate(new java.util.Date().getTime());
    		value.setLastModifiedBy(kpiValueRequest.getRequestInfo().getUserInfo().getId());
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
    	List<KpiTarget> kpiTargetList = kpiValueRepository.fetchTargetForKpi(kpiCode, finYear); 
    	if(null != kpiTargetList && kpiTargetList.size() > 0) { 
    		for(KpiTarget target : kpiTargetList) 
    			kpiTargetMap.put(target.getKpiCode(), target); 
    		return true; 
    	} else { 
    		return false; 
    	}
    }
	
	public boolean checkKpiValueExistsForTenant(String kpiCode, String tenantId) {
    	KpiValue value = kpiValueRepository.checkKpiValueExistsForTenant(kpiCode, tenantId); 
    	if(null != value && StringUtils.isNotBlank(value.getKpiCode()) && StringUtils.isNotBlank(value.getTenantId()))  
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
