package org.egov.pa.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.pa.model.Department;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiTarget;
import org.egov.pa.repository.KpiTargetRepository;
import org.egov.pa.repository.builder.PerformanceAssessmentQueryBuilder;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper.KPITargetRowMapper;
import org.egov.pa.validator.RestCallService;
import org.egov.pa.web.contract.KPITargetGetRequest;
import org.egov.pa.web.contract.KPITargetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("kpiTargetRepo")
@Slf4j
public class KpiTargetRepositoryImpl implements KpiTargetRepository {
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@Value("${kafka.topics.kpitarget.create.name}") 
	private String saveKpiTargetTopic; 
	
	@Value("${kafka.topics.kpitarget.update.name}")
	private String updateKpiTargetTopic; 
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private PerformanceAssessmentQueryBuilder queryBuilder;
    
    @Autowired
    private RestCallService restCallService; 
    
    private static final String MH_TENANT = "mh"; 
	
	@Override
	public void persistNewTarget(KPITargetRequest kpiTargetRequest) {
		log.info("Request before pushing to Kafka Queue : " + kpiTargetRequest);
    	kafkaTemplate.send(saveKpiTargetTopic, kpiTargetRequest);
	}

	@Override
	public void modifyNewTarget(KPITargetRequest kpiTargetRequest) {
		log.info("Request before pushing to Kafka Queue : " + kpiTargetRequest);
    	kafkaTemplate.send(updateKpiTargetTopic, kpiTargetRequest);
	}

	@Override
	public List<KpiTarget> searchKpiTargets(KPITargetGetRequest getReq) {
		final List<Object> preparedStatementValues = new ArrayList<>();
    	String query = queryBuilder.getTargetSearchQuery(getReq, preparedStatementValues);
    	KPITargetRowMapper mapper = new PerformanceAssessmentRowMapper().new KPITargetRowMapper(); 
    	List<KpiTarget> list = jdbcTemplate.query(query, preparedStatementValues.toArray(), mapper);
    	Map<String, KPI> kpiMap = mapper.kpiMap; 
    	List<Department> deptList = null ;
    	
    	if(StringUtils.isNotBlank(getReq.getTenantId())){
			if(getReq.getTenantId().split("\\.")[0].equals(MH_TENANT)) { 
				deptList = restCallService.getDepartmentForId(MH_TENANT);
			} else { 
				deptList = restCallService.getDepartmentForId(getReq.getTenantId());
			}
		} else {
			deptList = restCallService.getDepartmentForId(MH_TENANT);
		}
    	
    	Map<String, Department> deptMap = new HashMap<>(); 
    	sortDepartmentToMap(deptList, deptMap);
    	if(null != list && list.size() > 0) {
    		for(KpiTarget target : list) { 
    			KPI kpi = kpiMap.get(target.getKpiCode());
    			try {
    				arrangeDeptToKpi(kpi, deptMap);
    			}
    			catch (Exception e) {
    				log.error("Encountered an exception while adding Department : " + e);
    			}
    			target.setKpi(kpi); 
    		}
    	}
		return list;
	}
	
	private void arrangeDeptToKpi(KPI kpi, Map<String, Department> deptMap) {
		kpi.setDepartment(deptMap.get(String.valueOf(kpi.getDepartmentId())));
	}
	
	private void sortDepartmentToMap(List<Department> deptList, Map<String, Department> deptMap) { 
		for(Department dept : deptList) { 
			deptMap.put(dept.getId(), dept); 
		}
	}

	@Override
	public boolean checkActualValuesForKpi(List<String> kpiCodeList) {
		String query = queryBuilder.checkActualValueForKpi();
		log.info("Query to run for checkActualValuesForKpi : " + query);
		Map<String, Object> paramValues = new HashMap<>();
		int count = 0;
		paramValues.put("kpiCodeList", kpiCodeList);
		try {
			count = namedParameterJdbcTemplate.queryForObject(query, paramValues, Integer.class);
		} catch (Exception e) {
			throw new RuntimeException("Count of KPI Actual Values unavailable for some reason");
		}
		log.info("Count obtained after query  : "  + count);
		if (count > 0)
			return true;
		return false;
	}
	

}
