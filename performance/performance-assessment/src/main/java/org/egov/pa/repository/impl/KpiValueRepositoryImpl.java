package org.egov.pa.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.KpiValueList;
import org.egov.pa.model.ValueDocument;
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

	@Override
	public void persistKpiValue(KPIValueRequest kpiValueRequest) {
		log.info("Request before pushing to Kafka Queue : " + kpiValueRequest);
    	kafkaTemplate.send(createKpiValueTopic, kpiValueRequest);	
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
		String query = queryBuilder.getValueCompareSearchQuery(kpiValueSearchReq, preparedStatementValues);
		KPIValueRowMapper mapper = new PerformanceAssessmentRowMapper().new KPIValueRowMapper();
		List<KpiValue> listOfValues = jdbcTemplate.query(query, preparedStatementValues.toArray(), mapper);
		if(listOfValues.size() == 1) { 
			String docQuery = queryBuilder.getDocumentForKpiValue();
			for(KpiValue value : listOfValues) { 
				final HashMap<String, Object> parametersMap = new HashMap<>(); 
				parametersMap.put("vid", value.getId());
				List<ValueDocument> docList = namedParameterJdbcTemplate.query(docQuery, parametersMap, new BeanPropertyRowMapper<>(ValueDocument.class));
				value.setDocuments(docList);
			}
		}
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
