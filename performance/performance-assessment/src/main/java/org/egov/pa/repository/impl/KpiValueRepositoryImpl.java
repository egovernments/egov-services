package org.egov.pa.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.KpiValueDetail;
import org.egov.pa.model.KpiValueList;
import org.egov.pa.repository.KpiValueRepository;
import org.egov.pa.repository.builder.PerformanceAssessmentQueryBuilder;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper.KPIValueListRowMapper;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper.KPIValueRowMapper;
import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("kpiValueRepo")
@Slf4j
public class KpiValueRepositoryImpl implements KpiValueRepository{
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PerformanceAssessmentQueryBuilder queryBuilder; 
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Value("${kafka.topics.kpivalue.create.name}")
	private String createKpiValueTopic;
    
    @Value("${kafka.topics.kpivalue.update.name}")
	private String updateKpiValueTopic;
    
    @Value("${kafka.topics.kpivaluedetail.create.name}")
    private String kpiValueDetailCreateTopic;
    
    @Value("${kafka.topics.kpivaluedetail.delete.name}")
    private String kpiValueDetailDeleteTopic; 
    
	@Override
	public void persistKpiValue(KPIValueRequest kpiValueRequest) {
		log.info("Request before pushing to Kafka Queue : " + kpiValueRequest);
    	kafkaTemplate.send(createKpiValueTopic, kpiValueRequest);	
	}
	
	@Override
	public void persistKpiValueDetail(KPIValueRequest kpiValueRequest) {
		log.info("Request before pushing to Kafka Queue : " + kpiValueRequest);
    	kafkaTemplate.send(kpiValueDetailCreateTopic, kpiValueRequest);
	}
	
	@Override
	public void updateKpiValue(KPIValueRequest kpiValueRequest) {
		log.info("Request before pushing to Kafka Queue : " + kpiValueRequest);
    	kafkaTemplate.send(updateKpiValueTopic, kpiValueRequest);
	}

	@Override
	public List<KpiValueList> compareSearchKpiValue(KPIValueSearchRequest kpiValueSearchReq) {
		final List<Object> preparedStatementValues = new ArrayList<>();
    	String query = queryBuilder.getValueCompareSearchQuery(kpiValueSearchReq,preparedStatementValues);
    	KPIValueListRowMapper mapper = new PerformanceAssessmentRowMapper().new KPIValueListRowMapper(); 
    	List<KpiValueList> listOfValues = jdbcTemplate.query(query, preparedStatementValues.toArray(), mapper);
    	return listOfValues;
	}
	
	@Override
	public List<KpiValue> searchKpiValue(KPIValueSearchRequest kpiValueSearchReq) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		String query = queryBuilder.getValueSearchQuery(kpiValueSearchReq, preparedStatementValues);
		KPIValueRowMapper mapper = new PerformanceAssessmentRowMapper().new KPIValueRowMapper();
		jdbcTemplate.query(query, preparedStatementValues.toArray(), mapper);
		List<KpiValue> listOfValues = new ArrayList<>(); 
		Map<String, KpiValue> valueMap = mapper.valueMap; 
		Iterator<Entry<String, KpiValue>> itr = valueMap.entrySet().iterator();

		while(itr.hasNext()) { 
			Entry<String, KpiValue> entry = itr.next();
			String id = entry.getKey(); 
			KpiValue value = entry.getValue();
			
			if(mapper.valueDetailMap.containsKey(id)) { 
				List<KpiValueDetail> valueDetailList = mapper.valueDetailMap.get(id); 
				value.setValueList(valueDetailList);
			} else { 
				List<KpiValueDetail> defaultValueDetailList = mapper.valueDetailMap.get(value.getKpiCode()+"_"+value.getTenantId());
				value.setValueList(defaultValueDetailList);
			}
			listOfValues.add(value);
		}
		log.info("Size of Kpi Value List :  " + listOfValues.size());
		log.info("Kpi Value List :  " + listOfValues.toString());
		return listOfValues;
	}

	@Override
	public List<KPI> checkKpiExists(String kpiCode) {
		String query = PerformanceAssessmentQueryBuilder.fetchKpiByCode();
		final HashMap<String, Object> parametersMap = new HashMap<>();
		parametersMap.put("code", kpiCode);
		List<KPI> list = namedParameterJdbcTemplate.query(query, parametersMap, new BeanPropertyRowMapper<>(KPI.class));
		return list;
	}

	@Override
	public List<KPI> fetchTargetForKpi(String kpiCode, String finYear) {
		String query = PerformanceAssessmentQueryBuilder.fetchTargetForKpi(); 
		final HashMap<String, Object> parametersMap = new HashMap<>(); 
		parametersMap.put("kpiCode", kpiCode); 
		parametersMap.put("finYear", finYear);
		List<KPI> list = namedParameterJdbcTemplate.query(query, parametersMap, new BeanPropertyRowMapper<>(KPI.class));
		return list;
	}

	@Override
	public Long checkKpiValueExistsForTenant(String kpiCode, String tenantId) {
		String query = PerformanceAssessmentQueryBuilder.fetchKpiValueForCodeAndTenant(); 
		final HashMap<String, Object> parametersMap = new HashMap<>(); 
		parametersMap.put("kpiCode", kpiCode); 
		parametersMap.put("tenantId", tenantId);
		Long resultValue = 0l;  
		try { 
			resultValue = namedParameterJdbcTemplate.queryForObject(query, parametersMap, Long.class);			
		} catch(EmptyResultDataAccessException exec) { 
			log.info("Empty Data Set resulted - Code and Tenant ID does not have a record : " + exec);
		} catch(Exception e) { 
			log.error("Encountered an exception while fetching the data for Code and Tenant Id : " + e);
		}
		return resultValue;
	}

	@Override
	public String searchPossibilityCheck(String tenantCount, String kpiCount, String finYearCount) {
		String query = PerformanceAssessmentQueryBuilder.queryForSearchConfig();
		final HashMap<String, Object> parametersMap = new HashMap<>(); 
		parametersMap.put("tenant", tenantCount); 
		parametersMap.put("kpi", kpiCount);
		parametersMap.put("finYear", finYearCount);
		String possibilityCheck = null;
		try { 
			possibilityCheck = namedParameterJdbcTemplate.queryForObject(query, parametersMap, String.class);			
		} catch(EmptyResultDataAccessException exec) { 
			log.info("Empty Data Set resulted - Code and Tenant ID does not have a record : " + exec);
		} catch(Exception e) { 
			log.error("Encountered an exception while fetching the data for Code and Tenant Id : " + e);
		}
		return possibilityCheck;
	}

	@Override
	public List<Long> getNewKpiIds(int numberOfIds) {
		String query = PerformanceAssessmentQueryBuilder.getNextKpiValueId();
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
	public int numberOfDocsRequired(String kpiCode) {
		String query = PerformanceAssessmentQueryBuilder.numberOfDocsReqQuery();
		Map<String, Object> paramValues = new HashMap<>();
		paramValues.put("kpiCode", kpiCode);
		int numberOfDocsReq = 0; 
		try {
			numberOfDocsReq = namedParameterJdbcTemplate.queryForObject(query, paramValues, Integer.class);
		} catch (Exception e) {
		}
		return numberOfDocsReq;
	}

	

}
