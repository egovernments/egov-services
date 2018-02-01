package org.egov.pa.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.pa.model.AuditDetails;
import org.egov.pa.model.Document;
import org.egov.pa.model.DocumentTypeContract;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiCategory;
import org.egov.pa.model.KpiTarget;
import org.egov.pa.model.KpiTargetList;
import org.egov.pa.repository.KpiMasterRepository;
import org.egov.pa.service.KpiMasterService;
import org.egov.pa.validator.RestCallService;
import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("kpiMasterServ")
@Slf4j
public class KpiMasterServiceImpl implements KpiMasterService {
	
	@Autowired 
	@Qualifier("kpiMasterRepo")
	private KpiMasterRepository kpiMasterRepository;
	
	@Autowired
	private RestCallService restCallService; 
	
	@Override
	public KPIRequest createNewKpi(KPIRequest kpiRequest) {
		int numberOfIds = kpiRequest.getKpIs().size(); 
    	log.info("KPI Message Received at Service Level : " + kpiRequest);
    	List<Long> kpiIdList = kpiMasterRepository.getNewKpiIds(numberOfIds);
    	log.info("KPI Master Next ID Generated is : " + kpiIdList);
    	if(kpiIdList.size() == kpiRequest.getKpIs().size()) { 
    		for(int i = 0 ; i < kpiRequest.getKpIs().size() ; i++) { 
    			kpiRequest.getKpIs().get(i).setId(String.valueOf(kpiIdList.get(i)));
    		}
    	}
    	setCreatedDateAndUpdatedDate(kpiRequest);
    	prepareDocumentObjects(kpiRequest);
    	autoGenerateKpiCode(kpiRequest);
    	kpiMasterRepository.persistKpi(kpiRequest);
    	return kpiRequest;
	}
	
	private void autoGenerateKpiCode(KPIRequest kpiRequest) { 
		List<KPI> kpiList = kpiRequest.getKpIs(); 
		StringBuilder kpiCode = new StringBuilder(); 
		for(KPI kpi : kpiList) { 
			String[] kpiName = kpi.getName().split("\\s");
			if(kpiName.length > 0) { 
				for(String word : kpiName) {
					kpiCode.append(String.valueOf(word.charAt(0)).toUpperCase()); 
				}
			} else { 
				kpiCode.append(kpiName[0].toUpperCase());
			}
			kpiCode.append("_"+kpi.getFinancialYear()+"_"+String.valueOf(new Date().getTime()).substring(8, 13));
			kpi.setCode(kpiCode.toString());
		}
		
	}
	
	@Override
	public KPIRequest updateNewKpi(KPIRequest kpiRequest) {
		log.info("KPI Message Received at Service Level : " + kpiRequest);
    	List<KpiTarget> updateList = new ArrayList<>(); 
    	List<KpiTarget> insertList = new ArrayList<>();
    	searchKpiTarget(kpiRequest, updateList, insertList);
    	log.info("KPI Targets to be updated : " + updateList);
    	log.info("KPI Targets to be inserted : " + insertList);
    	setCreatedDateAndUpdatedDate(kpiRequest);
    	prepareDocumentObjects(kpiRequest);
    	kpiMasterRepository.updateKpi(kpiRequest);
    	if(updateList.size() > 0) { 
    		KpiTargetList targetList = new KpiTargetList(); 
    		targetList.setTargetList(updateList);
    		kpiMasterRepository.updateKpiTarget(targetList);
    	} 
    	if(insertList.size() > 0) { 
    		KpiTargetList targetList = new KpiTargetList(); 
    		targetList.setTargetList(insertList);
    		kpiMasterRepository.persistKpiTarget(targetList);
    	}
    	return kpiRequest;
	}
	
	@Override
	public KPIRequest deleteNewKpi(KPIRequest kpiRequest) {
		log.info("KPI Message Received at Service Level : " + kpiRequest);
    	setCreatedDateAndUpdatedDate(kpiRequest);
    	kpiMasterRepository.deleteKpi(kpiRequest);
    	return kpiRequest;
	}
	
	@Override
	public List<KPI> searchKpi(KPIGetRequest kpiGetRequest) {
		log.info("KPI Get Request Received at Service Level : " + kpiGetRequest); 
		Map<String, KpiCategory> kpiCategoryMap = restCallService.getCategory(kpiGetRequest.getTenantId());
		List<KPI> kpiList = kpiMasterRepository.searchKpi(kpiGetRequest);
		for(KPI kpi : kpiList) { 
			if(null != kpiCategoryMap.get(kpi.getCategoryId()) && StringUtils.isNotBlank(kpiCategoryMap.get(kpi.getCategoryId()).getName())) { 
				kpi.setCategory(kpiCategoryMap.get(kpi.getCategoryId()).getName());
			}
		}
    	return kpiList;
	}
	
	
	private void searchKpiTarget(KPIRequest kpiRequest, List<KpiTarget> updateList, List<KpiTarget> insertList) { 
    	/*for(KPI kpi : kpiRequest.getKpIs()) { 
    		if(null != kpi.getKpiTarget()) {
    			KpiTarget kpiTarget = kpi.getKpiTarget();
    			if(null != kpiTarget.getId()) { 
    				updateList.add(kpiTarget); 
    			} else { 
    				kpiTarget.setCreatedBy(kpiRequest.getRequestInfo().getUserInfo().getId());
    				insertList.add(kpiTarget);
    			}
    		}
    	}*/
    }
	
	@Override
	public Boolean getKpiType(String kpiCode, String tenantId) {
		return kpiMasterRepository.getKpiType(kpiCode, tenantId);
	}
	
	private void prepareDocumentObjects(KPIRequest kpiRequest) { 
    	List<KPI> kpiList = kpiRequest.getKpIs(); 
    	for(KPI kpi : kpiList) {
    		if(null != kpi.getDocuments() && kpi.getDocuments().size() > 0) {
    			for(int i=0 ; i< kpi.getDocuments().size() ; i++) { 
    				kpi.getDocuments().get(i).setKpiCode(kpi.getCode());
    				kpi.getDocuments().get(i).setCode(kpi.getCode().concat("_"+i+"_") + new Date().getTime());
    			}
    		}
    	}
    }
    
    private void setCreatedDateAndUpdatedDate(KPIRequest kpiRequest) { 
    	List<KPI> kpiList = kpiRequest.getKpIs();
    	for(KPI kpi : kpiList) { 
    		AuditDetails audit = new AuditDetails(); 
    		audit.setCreatedTime(new java.util.Date().getTime());
    		audit.setLastModifiedTime(new java.util.Date().getTime());
    		kpi.setAuditDetails(audit);
    		if(null != kpi.getDocuments() && kpi.getDocuments().size() > 0) { 
    			for(Document doc : kpi.getDocuments()) { 
    				doc.setAuditDetails(audit);
        		}
    		}
    	}
    }
    
    public boolean checkNameOrCodeExists(KPIRequest kpiRequest, Boolean createOrUpdate) { 
    	List<KPI> kpiList = kpiMasterRepository.checkNameOrCodeExists(kpiRequest);
    	if(!createOrUpdate) { 
    		for(KPI kpi : kpiList) { 
    			for(int i=0 ; i<kpiRequest.getKpIs().size() ; i++) {
    				if(kpiRequest.getKpIs().get(i).getId().equals(kpi.getId())) {
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
    
    public String targetAlreadyAvailable(String kpiCode) { 
    	return kpiMasterRepository.targetExistsForKPI(kpiCode); 
    }

	@Override
	public List<DocumentTypeContract> getDocumentForKpi(String kpiCode) {
		return kpiMasterRepository.getDocumentForKpi(kpiCode);
	}

	
}
