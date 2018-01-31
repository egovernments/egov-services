package org.egov.pa.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.egov.pa.model.Department;
import org.egov.pa.model.Document;
import org.egov.pa.model.DocumentTypeContract;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiCategory;
import org.egov.pa.model.KpiTarget;
import org.egov.pa.model.KpiTargetList;
import org.egov.pa.repository.KpiMasterRepository;
import org.egov.pa.repository.builder.PerformanceAssessmentQueryBuilder;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper.KPIMasterRowMapper;
import org.egov.pa.validator.RestCallService;
import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
    
    private static final String MH_TENANT = "mh"; 

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
		String query = PerformanceAssessmentQueryBuilder.getNextKpiMasterId();
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
	public Boolean getKpiType(String kpiCode, String tenantId) {
		String query = queryBuilder.getKpiTypeQuery(); 
		final List<Object> preparedStatementValues = new ArrayList<>();
		preparedStatementValues.add(kpiCode);
		Boolean targetType = jdbcTemplate.queryForObject(query, preparedStatementValues.toArray(), Boolean.class);
		return targetType;
	}

	@Override
	public List<KPI> searchKpi(KPIGetRequest kpiGetRequest) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		List<Department> deptList = null;
		if(StringUtils.isNotBlank(kpiGetRequest.getTenantId())){
			if(kpiGetRequest.getTenantId().split("\\.")[0].equals(MH_TENANT)) { 
				deptList = restCallService.getDepartmentForId(MH_TENANT);
			} else { 
				deptList = restCallService.getDepartmentForId(kpiGetRequest.getTenantId());
			}
		} else {
			deptList = restCallService.getDepartmentForId(MH_TENANT);
		}
		
		log.info("Department List obtained for the Tenant ID : " + deptList.toString());
    	String query = queryBuilder.getKpiSearchQuery(kpiGetRequest, preparedStatementValues);
    	KPIMasterRowMapper mapper = new PerformanceAssessmentRowMapper().new KPIMasterRowMapper(); 
    	jdbcTemplate.query(query, preparedStatementValues.toArray(), mapper);
    	List<KPI> kpiList = new ArrayList<>();
		Map<String, KPI> kpiMap = mapper.kpiMap; 
		Iterator<Entry<String, KPI>> itr = kpiMap.entrySet().iterator();
		while(itr.hasNext()) { 
			kpiList.add(itr.next().getValue()); 
		}
    	log.info("Number of KPIs Obtainted : " + kpiList.size()); 
    	if(null != kpiList && kpiList.size() > 0) { 
    		arrangeDeptToKpiList(kpiList, deptList);
    		if(kpiList.size() == 1) { 
    			List<String> kpiCodeList = new ArrayList<>();
    			kpiCodeList.add(kpiList.get(0).getCode()); 
    			kpiList.get(0).setDocuments(getDocumentListForKpi(kpiCodeList)); 
    		}
    	}
    	return kpiList; 
	}
	
	@Override
	public List<KPI> getKpiByCode(Boolean getTargets, List<String> kpiCodeList, KPIValueSearchRequest kpiValueSearchReq) {
		Map<String, KpiCategory> categoryMap = new HashMap<>(); 
		for(String tenant : kpiValueSearchReq.getTenantId()) { 
			categoryMap = restCallService.getCategory(tenant);
		}
		final List<Object> preparedStatementValues = new ArrayList<>();
		List<String> finYearList = kpiValueSearchReq.getFinYear(); 
		Long departmentId = kpiValueSearchReq.getDepartmentId();
		Long categoryId = kpiValueSearchReq.getCategoryId(); 
		String query = queryBuilder.getKpiByCode(kpiCodeList, finYearList, departmentId, categoryId, preparedStatementValues);
		log.info("QUERY to fetch KPI Details : " + query);
		KPIMasterRowMapper mapper = new PerformanceAssessmentRowMapper().new KPIMasterRowMapper();
		jdbcTemplate.query(query, preparedStatementValues.toArray(), mapper);
		List<KPI> kpiList = new ArrayList<>();
		Map<String, KPI> kpiMap = mapper.kpiMap;
		List<String> availableKpiCodeList = new ArrayList<>();
		Iterator<Entry<String, KPI>> itr = kpiMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, KPI> kpiEntry = itr.next();
			availableKpiCodeList.add(kpiEntry.getKey());  
			kpiList.add(kpiEntry.getValue());
		}
		if(availableKpiCodeList.size()>0) { 
			List<Document> docList = new ArrayList<>();
			docList = getDocumentListForKpi(availableKpiCodeList);
			for(KPI kpi : kpiList) {
				if(null != categoryMap.get(kpi.getCategoryId()) && StringUtils.isNotBlank(categoryMap.get(kpi.getCategoryId()).getName()))
					kpi.setCategory(categoryMap.get(kpi.getCategoryId()).getName());
				List<Document> kpiDocList = new ArrayList<>(); 
				kpi.setDocuments(kpiDocList);
				for(Document doc : docList) { 
					if(kpi.getCode().equals(doc.getKpiCode())) { 
						kpi.getDocuments().add(doc);
					}
				}
			}
		}
		if (getTargets) {
			mapTargetToKpi(mapper.kpiTargetMap, kpiList);
		}
		return kpiList;
	}
	
	private void mapTargetToKpi(Map<String, List<KpiTarget>> map, List<KPI> kpiList){
		for(KPI kpi : kpiList) { 
			List<KpiTarget> targetList = map.get(kpi.getCode());
			if(null != targetList && targetList.size() > 0) { 
				kpi.setKpiTargets(targetList);
			}
			
		}
	}
	
	private List<Document> getDocumentListForKpi(List<String> kpiCode) {
		String query = queryBuilder.getDocumentForKpi(); 
		final HashMap<String, Object> parametersMap = new HashMap<>();
		parametersMap.put("kpiCode", kpiCode);
		List<Document> docList = namedParameterJdbcTemplate.query(query,
                parametersMap, new BeanPropertyRowMapper<>(Document.class));
		return docList;
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
	
	
	
	private void arrangeDeptToKpiList(List<KPI> kpiList, List<Department> deptList) {
		Map<String, Department> deptMap = new HashMap<>();
		if (null != deptList && deptList.size() > 0) {
			sortDepartmentToMap(deptList, deptMap);
			for (int i = 0; i < kpiList.size(); i++) {
				kpiList.get(i).setDepartment(deptMap.get(String.valueOf(kpiList.get(i).getDepartmentId()))); 
			}
		}
	}
	
	/*private void arrangeListDepartmentWise(List<KPI> kpiList, List<Department> deptList, List<DepartmentKpiList> deptWiseList) { 
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
    }*/
	
	private void sortDepartmentToMap(List<Department> deptList, Map<String, Department> deptMap) { 
		for(Department dept : deptList) { 
			deptMap.put(dept.getId(), dept); 
		}
	}
	
	@Override
	public List<String> getTargetTypeForKpiCodes(List<String> kpiCodeList) { 
		final HashMap<String, Object> parametersMap = new HashMap<>();
		String query = queryBuilder.getTargetTypeForKpi(); 
		parametersMap.put("kpiCodeList", kpiCodeList);
		List<String> targetTypeList= namedParameterJdbcTemplate.query(query,
                parametersMap, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("targettype");
			}
		});
		return targetTypeList;
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
	
	@Override
	public String targetExistsForKPI(String kpiCode) { 
		String query = queryBuilder.targetAvailableForKpi(); 
		final HashMap<String, Object> parametersMap = new HashMap<>();
		parametersMap.put("kpiCode", kpiCode);
		String targetType = ""; 
		try { 
			targetType = namedParameterJdbcTemplate.queryForObject(query,parametersMap, String.class);
		} catch(EmptyResultDataAccessException erdae) { 
			log.info("No target has been set for the KPI : " + kpiCode); 
		} catch(Exception e) { 
			log.error("Encountered an exception while fetching the target for the KPI : " + kpiCode + " :: Exception : " + e) ; 
		}
		
		return targetType; 
	}

	@Override
	public List<DocumentTypeContract> getDocumentForKpi(String kpiCode) {
		String query = queryBuilder.getDocumentForKpi();
		final HashMap<String, Object> parametersMap = new HashMap<>();
		parametersMap.put("kpiCode", kpiCode);
		List<DocumentTypeContract> documentList = namedParameterJdbcTemplate.query(query,parametersMap, new BeanPropertyRowMapper<>(DocumentTypeContract.class));
		return documentList;
	}

	

}
