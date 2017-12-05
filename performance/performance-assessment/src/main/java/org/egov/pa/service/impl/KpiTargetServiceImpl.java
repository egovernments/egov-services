package org.egov.pa.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.pa.model.AuditDetails;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiTarget;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.Tenant;
import org.egov.pa.repository.KpiMasterRepository;
import org.egov.pa.repository.KpiTargetRepository;
import org.egov.pa.repository.KpiValueRepository;
import org.egov.pa.service.KpiTargetService;
import org.egov.pa.validator.RestCallService;
import org.egov.pa.web.contract.KPITargetGetRequest;
import org.egov.pa.web.contract.KPITargetRequest;
import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.ValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("kpiTargetServ")
@Slf4j
public class KpiTargetServiceImpl implements KpiTargetService {
	
	@Autowired 
	@Qualifier("kpiTargetRepo")
	private KpiTargetRepository kpiTargetRepository;
	
	@Autowired 
	@Qualifier("kpiValueRepo")
	private KpiValueRepository kpiValueRepository;
	
	@Autowired
	@Qualifier("kpiMasterRepo")
	private KpiMasterRepository kpiMasterRepository;
	
	@Autowired
	private RestCallService restCallService; 
	
	@Override
	public KPITargetRequest createNewTarget(KPITargetRequest kpiTargetRequest) {
		setCreatedAndUpdatedDate(kpiTargetRequest);
		log.info("KPI Target Message after updating Created Date " + kpiTargetRequest);
		kpiTargetRepository.persistNewTarget(kpiTargetRequest);
		kpiValueRepository.persistKpiValue(decideTenantsForTarget(kpiTargetRequest));
		return kpiTargetRequest;
	}

	@Override
	public KPITargetRequest updateNewTarget(KPITargetRequest kpiTargetRequest) {
		setCreatedAndUpdatedDate(kpiTargetRequest);
		log.info("KPI Target Message after updating Updated Date " + kpiTargetRequest);
		kpiTargetRepository.modifyNewTarget(kpiTargetRequest);
		return kpiTargetRequest;
	}
	
	private KPIValueRequest decideTenantsForTarget(KPITargetRequest targetRequest) {
		List<Tenant> tenantList = restCallService.getAllTenants();
		List<ValueResponse> valueResponseList = new ArrayList<>();
		for(KpiTarget target : targetRequest.getKpiTargets()) {
			if(StringUtils.isNotBlank(target.getTenantId())){
				KPI kpi = new KPI();
				kpi.setCode(target.getKpiCode());
				ValueResponse vr = new ValueResponse(); 
				vr.setTenantId(target.getTenantId());
				vr.setFinYear(target.getFinYear());
				vr.setKpi(kpi);
				AuditDetails audit = new AuditDetails(); 
				audit.setCreatedBy(targetRequest.getRequestInfo().getUserInfo().getId());
				audit.setCreatedTime(target.getCreatedDate());
				KpiValue value = new KpiValue();
				value.setAuditDetails(audit);
				vr.setKpiValue(value);
				
				valueResponseList.add(vr); 
			} else { 
				for(Tenant tenant : tenantList) { 
					KPI kpi = new KPI();
					kpi.setCode(target.getKpiCode());
					ValueResponse vr = new ValueResponse(); 
					vr.setTenantId(tenant.getCode());
					vr.setFinYear(target.getFinYear());
					vr.setKpi(kpi);
					AuditDetails audit = new AuditDetails(); 
					audit.setCreatedBy(targetRequest.getRequestInfo().getUserInfo().getId());
					audit.setCreatedTime(target.getCreatedDate());
					KpiValue value = new KpiValue();
					value.setAuditDetails(audit);
					vr.setKpiValue(value);
					
					valueResponseList.add(vr);
				}
			}
		}
		KPIValueRequest kpiValueRequest = new KPIValueRequest(); 
		kpiValueRequest.setRequestInfo(targetRequest.getRequestInfo());
		kpiValueRequest.setKpiValues(valueResponseList);
		return kpiValueRequest;
	}
	
	
	private void setCreatedAndUpdatedDate(KPITargetRequest targetRequest) {
		for(KpiTarget target : targetRequest.getKpiTargets()) {
			target.setCreatedDate(new java.util.Date().getTime());
			target.setLastModifiedDate(new java.util.Date().getTime());
		}
	}

	@Override
	public List<KpiTarget> searchKpiTarget(KPITargetGetRequest getReq) {
		List<KpiTarget> targetList = kpiTargetRepository.searchKpiTargets(getReq);
		List<String> kpiCodeList = new ArrayList<>();
		for(KpiTarget target : targetList) { 
			kpiCodeList.add(target.getKpiCode());
		}
		List<KPI> kpiList = kpiMasterRepository.getKpiByCode(kpiCodeList, getReq.getFinYear());
		sortKpisToTarget(targetList, kpiList);
		return targetList;
	}
	
	private void sortKpisToTarget(List<KpiTarget> targetList, List<KPI> kpiList) { 
		for(KpiTarget target : targetList) { 
			for(KPI kpi : kpiList) { 
				if(target.getKpiCode().equals(kpi.getCode())) { 
					target.setKpi(kpi);
				}
			}
		}
	}
}
