/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pa.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.pa.model.Document;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiTarget;
import org.egov.pa.model.KpiTargetList;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.KpiValueList;
import org.egov.pa.repository.PerformanceAssessmentRepository;
import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIRequest;
import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PerformanceAssessmentService {
	
	public static Map<String, KPI> kpiMap = new HashMap<>();
	public static Map<String, KpiTarget> kpiTargetMap = new HashMap<>();

    @Autowired
    private PerformanceAssessmentRepository performanceAssessmentRepository;

    public static final String roleCode = "CITIZEN";
    public static final String roleName = "Citizen";
    
    public KPIRequest createNewKpi(KPIRequest kpiRequest) {
    	int numberOfIds = kpiRequest.getKpis().size(); 
    	log.info("KPI Message Received at Service Level : " + kpiRequest);
    	List<Long> kpiIdList = performanceAssessmentRepository.getNewKpiIds(numberOfIds);
    	log.info("KPI Master Next ID Generated is : " + kpiIdList);
    	if(kpiIdList.size() == kpiRequest.getKpis().size()) { 
    		for(int i = 0 ; i < kpiRequest.getKpis().size() ; i++) { 
    			kpiRequest.getKpis().get(i).setId(kpiIdList.get(i));
    		}
    	}
    	setCreatedDateAndUpdatedDate(kpiRequest);
    	prepareDocumentObjects(kpiRequest);
    	performanceAssessmentRepository.persistKpi(kpiRequest);
    	return kpiRequest;
    }
    
    public KPIRequest updateNewKpi(KPIRequest kpiRequest) {
    	log.info("KPI Message Received at Service Level : " + kpiRequest);
    	List<KpiTarget> updateList = new ArrayList<>(); 
    	List<KpiTarget> insertList = new ArrayList<>();
    	searchKpiTarget(kpiRequest, updateList, insertList);
    	log.info("KPI Targets to be updated : " + updateList);
    	log.info("KPI Targets to be inserted : " + insertList);
    	setCreatedDateAndUpdatedDate(kpiRequest);
    	prepareDocumentObjects(kpiRequest);
    	performanceAssessmentRepository.updateKpi(kpiRequest);
    	if(updateList.size() > 0) { 
    		KpiTargetList targetList = new KpiTargetList(); 
    		targetList.setTargetList(updateList);
    		performanceAssessmentRepository.updateKpiTarget(targetList);
    	} 
    	if(insertList.size() > 0) { 
    		KpiTargetList targetList = new KpiTargetList(); 
    		targetList.setTargetList(insertList);
    		performanceAssessmentRepository.persistKpiTarget(targetList);
    	}
    	return kpiRequest;
    }
    
    public KPIRequest deleteNewKpi(KPIRequest kpiRequest) { 
    	log.info("KPI Message Received at Service Level : " + kpiRequest);
    	setCreatedDateAndUpdatedDate(kpiRequest);
    	performanceAssessmentRepository.deleteKpi(kpiRequest);
    	return kpiRequest;
    }
    
    public List<KPI> searchKpi(KPIGetRequest kpiGetRequest) { 
    	log.info("KPI Get Request Received at Service Level : " + kpiGetRequest); 
    	return performanceAssessmentRepository.searchKpi(kpiGetRequest);
    }
    
    public KPIValueRequest createKpiValue(KPIValueRequest kpiValueRequest) {
    	log.info("KPI Value Create Request Received at Service Level : " + kpiValueRequest);
    	setCreatedDateAndUpdatedDate(kpiValueRequest);
    	performanceAssessmentRepository.persistKpiValue(kpiValueRequest);
    	return kpiValueRequest; 
    }
    
    public KPIValueRequest updateKpiValue(KPIValueRequest kpiValueRequest) {
    	log.info("KPI Value Update Request Received at Service Level : " + kpiValueRequest);
    	setCreatedDateAndUpdatedDate(kpiValueRequest);
    	performanceAssessmentRepository.updateKpiValue(kpiValueRequest);
    	return kpiValueRequest; 
    }
    
    public List<KpiValueList> searchKpiValue(KPIValueSearchRequest kpiValueSearchReq) { 
    	return performanceAssessmentRepository.searchKpiValue(kpiValueSearchReq); 
    }
    
	public boolean checkNameOrCodeExists(KPIRequest kpiRequest, Boolean createOrUpdate) { 
    	List<KPI> kpiList = performanceAssessmentRepository.checkNameOrCodeExists(kpiRequest);
    	if(!createOrUpdate) { 
    		for(KPI kpi : kpiList) { 
    			for(int i=0 ; i<kpiRequest.getKpis().size() ; i++) {
    				if(kpiRequest.getKpis().get(i).getId() == kpi.getId()) {
        				return false; 
        			}
    			}
    		}
    	}
    	if(kpiList.size() > 0) 
    		return true;
    	else 
    		return false;
    }
    
    public boolean checkKpiExists(String kpiCode) { 
    	if(kpiMap.containsKey(kpiCode)) { 
    		return true;
    	}
    	List<KPI> kpiList = performanceAssessmentRepository.checkKpiExists(kpiCode); 
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
    	List<KpiTarget> kpiTargetList = performanceAssessmentRepository.fetchTargetForKpi(kpiCode, finYear); 
    	if(null != kpiTargetList && kpiTargetList.size() > 0) { 
    		for(KpiTarget target : kpiTargetList) 
    			kpiTargetMap.put(target.getKpiCode(), target); 
    		return true; 
    	} else { 
    		return false; 
    	}
    }
    
    public boolean checkKpiValueExistsForTenant(String kpiCode, String tenantId) {
    	KpiValue value = performanceAssessmentRepository.checkKpiValueExistsForTenant(kpiCode, tenantId); 
    	if(null != value && StringUtils.isNotBlank(value.getKpiCode()) && StringUtils.isNotBlank(value.getTenantId()))  
    		return true;
    	else
    		return false; 
    }
    
	public String searchPossibilityCheck(String tenantCount, String kpiCount, String finYearCount) {
		return performanceAssessmentRepository.searchPossibilityCheck(tenantCount, kpiCount, finYearCount);
	}
    
    private void searchKpiTarget(KPIRequest kpiRequest, List<KpiTarget> updateList, List<KpiTarget> insertList) { 
    	for(KPI kpi : kpiRequest.getKpis()) { 
    		if(null != kpi.getKpiTarget()) {
    			KpiTarget kpiTarget = kpi.getKpiTarget();
    			if(null != kpiTarget.getId()) { 
    				updateList.add(kpiTarget); 
    			} else { 
    				kpiTarget.setCreatedBy(kpiRequest.getRequestInfo().getUserInfo().getId());
    				insertList.add(kpiTarget);
    			}
    		}
    	}
    }
    
    private void prepareDocumentObjects(KPIRequest kpiRequest) { 
    	List<KPI> kpiList = kpiRequest.getKpis(); 
    	for(KPI kpi : kpiList) {
    		if(null != kpi.getDocuments() && kpi.getDocuments().size() > 0) { 
    			for(Document doc : kpi.getDocuments()) { 
    				doc.setKpiCode(kpi.getCode());
    			}
    		}
    	}
    }
    
    private void setCreatedDateAndUpdatedDate(KPIRequest kpiRequest) { 
    	List<KPI> kpiList = kpiRequest.getKpis();
    	for(KPI kpi : kpiList) { 
    		kpi.setCreatedDate(new java.util.Date().getTime());
        	kpi.setLastModifiedDate(new java.util.Date().getTime());
        	if(null != kpi.getKpiTarget()) { 
        		kpi.getKpiTarget().setCreatedDate(new java.util.Date().getTime());
        		kpi.getKpiTarget().setLastModifiedDate(new java.util.Date().getTime());
        	}
    	}
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
