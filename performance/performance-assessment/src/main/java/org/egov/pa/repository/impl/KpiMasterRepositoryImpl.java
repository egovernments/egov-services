package org.egov.pa.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.pa.model.Department;
import org.egov.pa.model.DepartmentKpiList;
import org.egov.pa.model.Document;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiTargetList;
import org.egov.pa.repository.KpiMasterRepository;
import org.egov.pa.repository.builder.PerformanceAssessmentQueryBuilder;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper.KPIMasterRowMapper;
import org.egov.pa.validator.RestCallService;
import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("kpiMasterRepo")
@Slf4j
public class KpiMasterRepositoryImpl implements KpiMasterRepository {
	
	@Autowired
	private RestCallService restCallService; 
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    private PerformanceAssessmentQueryBuilder queryBuilder;
	
	@Value("${kafka.topics.newkpi.create.name}")
	private String saveNewKpiTopic;
    
    @Value("${kafka.topics.newkpi.update.name}")
	private String updateNewKpiTopic;
    
    @Value("${kafka.topics.newkpi.delete.name}")
	private String deleteNewKpiTopic;
    
    @Value("${kafka.topics.kpitarget.create.name}")
	private String saveKpiTargetTopic;
    
    @Value("${kafka.topics.kpitarget.update.name}")
	private String updateKpiTargetTopic;

	@Override
	public void persistKpi(KPIRequest kpiRequest) {
		log.info("Request before pushing to Kafka Queue : " + kpiRequest);
    	kafkaTemplate.send(saveNewKpiTopic, kpiRequest);		
	}

	@Override
	public void updateKpi(KPIRequest kpiRequest) {
		log.info("Request before pushing to Kafka Queue : " + kpiRequest);
    	kafkaTemplate.send(updateNewKpiTopic, kpiRequest);
	}

	@Override
	public void deleteKpi(KPIRequest kpiRequest) {
		log.info("Request before pushing to Kafka Queue : " + kpiRequest);
    	kafkaTemplate.send(deleteNewKpiTopic, kpiRequest);
	}

	@Override
	public List<Long> getNewKpiIds(int numberOfIds) {
		String query = PerformanceAssessmentQueryBuilder.getNextKpiMasterId(numberOfIds);
		Map<String, Object> paramValues = new HashMap<>();
		paramValues.put("size", numberOfIds);
		List<Long> idList;
		try {
			idList = namedParameterJdbcTemplate.queryForList(query, paramValues, Long.class);
		} catch (Exception e) {
			throw new RuntimeException("Next id is not generated.");
		}
		return idList;
	}
	
	@Override
	public List<KPI> checkNameOrCodeExists(KPIRequest kpiRequest) {
		String query = PerformanceAssessmentQueryBuilder.fetchKpiByNameOrCode();
		List<KPI> kpiList = new ArrayList<>();
		for(int i=0 ; i < kpiRequest.getKpIs().size() ; i ++) { 
			final HashMap<String, Object> parametersMap = new HashMap<>();
			parametersMap.put("name", kpiRequest.getKpIs().get(i).getName());
			parametersMap.put("code", kpiRequest.getKpIs().get(i).getCode());
			parametersMap.put("finyear", kpiRequest.getKpIs().get(i).getFinancialYear());
			List<KPI> list = namedParameterJdbcTemplate.query(query,
	                parametersMap, new BeanPropertyRowMapper<>(KPI.class));
			kpiList.addAll(list); 
		}
		return kpiList;
	}

	@Override
	public List<DepartmentKpiList> searchKpi(KPIGetRequest kpiGetRequest) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		List<Department> deptList = restCallService.getDepartmentForId("mh");
		log.info("Department List obtained for the Tenant ID : " + deptList.toString());
    	String query = queryBuilder.getKpiSearchQuery(kpiGetRequest, preparedStatementValues); 
    	KPIMasterRowMapper mapper = new PerformanceAssessmentRowMapper().new KPIMasterRowMapper();
    	jdbcTemplate.query(query, preparedStatementValues.toArray(), mapper);
    	List<KPI> kpiList = getListFromMapper(mapper.kpiMap);
    	log.info("Number of KPIs Obtainted : " + kpiList.size()); 
    	List<DepartmentKpiList> deptWiseKpiList = new ArrayList<>();
    	if(null != kpiList && kpiList.size() > 0) { 
    		arrangeDocsToKpiList(kpiList, mapper.docMap);
    		arrangeListDepartmentWise(kpiList, deptList, deptWiseKpiList);
    	}
    	return deptWiseKpiList; 
	}
	
	private List<KPI> getListFromMapper(Map<String, KPI> kpiMap) { 
    	Iterator<Entry<String, KPI>> itr = kpiMap.entrySet().iterator();
    	List<KPI> kpiList = new ArrayList<>();
    	while(itr.hasNext()) { 
    		Entry<String, KPI> entry = itr.next();
    		kpiList.add(entry.getValue()); 
    	}    
    	return kpiList;
    }
	
	private void arrangeDocsToKpiList(List<KPI> kpiList, Map<String, List<Document>> map) { 
    	Iterator<Entry<String, List<Document>>> itr = map.entrySet().iterator();
    	while(itr.hasNext()) { 
    		Entry<String, List<Document>> entry = itr.next();
    		for(KPI kpi : kpiList) { 
    			if(kpi.getId().equals(entry.getKey())) { 
    				kpi.setDocuments(entry.getValue());
    			}
    		}
    	}
    }
	
	private void arrangeListDepartmentWise(List<KPI> kpiList, List<Department> deptList, List<DepartmentKpiList> deptWiseList) { 
		Map<Long, Department> deptMap = new HashMap<>(); 
		sortDepartmentToMap(deptList, deptMap);
    	Iterator<Entry<Long, Department>> itr =deptMap.entrySet().iterator();
    	while(itr.hasNext()) { 
    		Entry<Long, Department> entry = itr.next();
    		List<KPI> tempList = new ArrayList<>();
    		for(KPI kpi : kpiList) {
    			if(kpi.getDepartment().getId() == entry.getKey()) {
    				tempList.add(kpi); 
    			}
    		}
    		DepartmentKpiList deptKpi = new DepartmentKpiList(); 
    		deptKpi.setDepartment(entry.getValue());
    		deptKpi.setKpiList(tempList);
    		if(tempList.size() > 0) { 
    			deptWiseList.add(deptKpi);
    		}
    		
    	}
    	for(KPI kpi : kpiList) kpi.setDepartment(null); 
    }
	
	private void sortDepartmentToMap(List<Department> deptList, Map<Long, Department> deptMap) { 
		for(Department dept : deptList) { 
			deptMap.put(dept.getId(), dept); 
		}
	}

	@Override
	public void persistKpiTarget(KpiTargetList kpiTargetList) {
		log.info("Request before pushing to Kafka Queue : " + kpiTargetList);
    	kafkaTemplate.send(saveKpiTargetTopic, kpiTargetList);
	}

	@Override
	public void updateKpiTarget(KpiTargetList kpiTargetList) {
		log.info("Request before pushing to Kafka Queue : " + kpiTargetList);
    	kafkaTemplate.send(updateKpiTargetTopic, kpiTargetList);
	}

}
