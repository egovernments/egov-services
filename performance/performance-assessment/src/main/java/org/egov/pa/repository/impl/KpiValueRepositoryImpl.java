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
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.KpiValueDetail;
import org.egov.pa.model.TargetType;
import org.egov.pa.model.ULBKpiValueList;
import org.egov.pa.model.ValueDocument;
import org.egov.pa.model.ValueList;
import org.egov.pa.repository.KpiValueRepository;
import org.egov.pa.repository.builder.PerformanceAssessmentQueryBuilder;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper.KPIObjectiveReportMapper;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper.KPIValueReportMapper;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper.KPIValueRowMapper;
import org.egov.pa.web.contract.KPIValueRequest;
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
	public List<ULBKpiValueList> compareSearchKpiValue(KPIValueSearchRequest kpiValueSearchReq) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		String query = queryBuilder.getValueCompareSearchQuery(kpiValueSearchReq, preparedStatementValues);
		KPIValueReportMapper reportMapper = new PerformanceAssessmentRowMapper().new KPIValueReportMapper(); 
		jdbcTemplate.query(query, preparedStatementValues.toArray(), reportMapper);
		log.info("Values inside KPI Map : " + reportMapper.kpiMap.size());
		log.info("Values inside Report Map : " + reportMapper.reportMap.size());
		List<ULBKpiValueList> list = new ArrayList<>();
		sortKpiMapToList(reportMapper, list);
		return list; 
	}
	
	@Override
	public List<ULBKpiValueList> compareSearchObjectiveKpiValue(KPIValueSearchRequest kpiValueSearchReq) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		String query = queryBuilder.getValueCompareObjectiveSearchQuery(kpiValueSearchReq, preparedStatementValues);
		KPIObjectiveReportMapper reportMapper = new PerformanceAssessmentRowMapper().new KPIObjectiveReportMapper(); 
		jdbcTemplate.query(query, preparedStatementValues.toArray(), reportMapper);
		log.info("Values inside KPI Map : " + reportMapper.kpiMap.size());
		log.info("Values inside Report Map : " + reportMapper.reportMap.size());
		List<ULBKpiValueList> list = new ArrayList<>();
		sortKpiMapToList(reportMapper, list);
		return list; 
	}
	
	private void sortKpiMapToList(KPIValueReportMapper reportMapper, List<ULBKpiValueList> list) { 
		Map<String, Map<String, Map<String, KpiValue>>> firstMap = reportMapper.reportMap;
		Map<String, KPI> kpiMap = reportMapper.kpiMap; 
		Map<String, List<KpiValueDetail>> valueDetailMap = reportMapper.valueListMap; 
				
		Iterator<Entry<String, Map<String, Map<String, KpiValue>>>> firstItr = firstMap.entrySet().iterator();
		while(firstItr.hasNext()) { 
			ULBKpiValueList ulb = new ULBKpiValueList(); 
			Entry<String, Map<String, Map<String, KpiValue>>> firstEntry = firstItr.next();
			ulb.setUlbName(firstEntry.getKey());
			List<ValueList> innerList = new ArrayList<>(); ////////////////////////////////
			Map<String, Map<String, KpiValue>> secondMap = firstEntry.getValue();
			Iterator<Entry<String, Map<String, KpiValue>>> secondItr = secondMap.entrySet().iterator();
			while(secondItr.hasNext()) { 
				ValueList valueList = new ValueList(); 
				Entry<String, Map<String, KpiValue>> secondEntry = secondItr.next();
				valueList.setFinYear(secondEntry.getKey());
				List<KpiValue> kpiValues = new ArrayList<>(); 
				Map<String, KpiValue> thirdMap = secondEntry.getValue();
				Iterator<Entry<String, KpiValue>> thirdItr = thirdMap.entrySet().iterator();
				while(thirdItr.hasNext()) { 
					Entry<String, KpiValue> thirdEntry = thirdItr.next();
					KpiValue value = thirdEntry.getValue();
					
					List<KpiValueDetail> valueDetailList = valueDetailMap.get(value.getId());
					if(null != valueDetailList && valueDetailList.size() > 0){ 
						value.setValueList(valueDetailList);
						Long consolidatedValue = 0L; 
						for(KpiValueDetail eachDetail : valueDetailList) { 
							if(StringUtils.isNotBlank(eachDetail.getValue())) consolidatedValue = consolidatedValue + Long.parseLong(eachDetail.getValue()) ;   
						}
						value.setConsolidatedValue(String.valueOf(consolidatedValue));
						value.setValueDescription(String.valueOf(consolidatedValue));
					}
					
					
					KPI kpi = kpiMap.get(thirdEntry.getKey().concat("_"+valueList.getFinYear()));
					if(null != kpi) value.setKpi(kpi);
					kpiValues.add(value);
				}
				valueList.setKpiValueList(kpiValues);
				innerList.add(valueList);
			}
			ulb.setFinYearList(innerList);
			list.add(ulb);
		}
		
		log.info("Final List after Map to List conversion : " + list);
	}
	
	private void sortKpiMapToList(KPIObjectiveReportMapper reportMapper, List<ULBKpiValueList> list) { 
		Map<String, Map<String, Map<String, KpiValue>>> firstMap = reportMapper.reportMap;
		Map<String, KPI> kpiMap = reportMapper.kpiMap; 
		Iterator<Entry<String, Map<String, Map<String, KpiValue>>>> firstItr = firstMap.entrySet().iterator();
		while(firstItr.hasNext()) { 
			ULBKpiValueList ulb = new ULBKpiValueList(); 
			Entry<String, Map<String, Map<String, KpiValue>>> firstEntry = firstItr.next();
			ulb.setUlbName(firstEntry.getKey());
			List<ValueList> innerList = new ArrayList<>(); ////////////////////////////////
			Map<String, Map<String, KpiValue>> secondMap = firstEntry.getValue();
			Iterator<Entry<String, Map<String, KpiValue>>> secondItr = secondMap.entrySet().iterator();
			while(secondItr.hasNext()) { 
				ValueList valueList = new ValueList(); 
				Entry<String, Map<String, KpiValue>> secondEntry = secondItr.next();
				valueList.setFinYear(secondEntry.getKey());
				List<KpiValue> kpiValues = new ArrayList<>(); 
				Map<String, KpiValue> thirdMap = secondEntry.getValue();
				Iterator<Entry<String, KpiValue>> thirdItr = thirdMap.entrySet().iterator();
				while(thirdItr.hasNext()) { 
					Entry<String, KpiValue> thirdEntry = thirdItr.next();
					KpiValue value = thirdEntry.getValue();
					List<KpiValueDetail> detailList = reportMapper.kpiValueDetailMap.get(value.getId());
					if(null != detailList && detailList.size() == 12) { 
						for(KpiValueDetail detail : detailList) { 
							if(StringUtils.isNotBlank(detail.getValue())) { 
								value.setConsolidatedValue(detail.getValue());
								if(detail.getValue().equals(TargetType.OBJECTIVE.toString())) { 
									if(detail.getValue().equals("1")) 
										value.setValueDescription("Yes");
									else if(detail.getValue().equals("2"))
										value.setValueDescription("No");
									else if(detail.getValue().equals("3"))
										value.setValueDescription("Work In Progress");
								}
								value.setPeriod(detail.getPeriod());
								if(detail.getValue().equals("1")) value.setValueDescription("Yes");
								else if (detail.getValue().equals("2")) value.setValueDescription("No");
								else if (detail.getValue().equals("3")) value.setValueDescription("Work In Progress");
							}
						}
					} else { 
						List<KpiValueDetail> defaultValueDetailList = reportMapper.valueDetailMap.get(value.getKpiCode().concat("_"+value.getTenantId()+"_"+value.getFinYear()));
						value.setValueList(defaultValueDetailList);
					}
					KPI kpi = kpiMap.get(thirdEntry.getKey());
					if(null != kpi) value.setKpi(kpi);
					value.setValueList(detailList);
					kpiValues.add(value);
				}
				valueList.setKpiValueList(kpiValues);
				innerList.add(valueList);
			}
			ulb.setFinYearList(innerList);
			list.add(ulb);
		}
		log.info("Final List after Map to List conversion : " + list);
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
				for(KpiValueDetail detail : valueDetailList) {
					if(null != detail) 
						detail.setDocumentList(mapper.detailDocumentMap.get(detail.getId()));  
				}
				value.setValueList(valueDetailList);
				
			} else { 
				List<KpiValueDetail> defaultValueDetailList = mapper.valueDetailMap.get(value.getKpiCode()+"_"+value.getTenantId()+"_"+value.getFinYear());
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
			possibilityCheck = namedParameterJdbcTemplate.queryForObject(query, parametersMap, new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("possibility").concat("_" + rs.getString("graphtype"));
				}});			
		} catch(EmptyResultDataAccessException exec) { 
			log.info("Empty Data Set resulted - Code and Tenant ID does not have a record : " + exec);
		} catch(Exception e) { 
			log.error("Encountered an exception while fetching the data for Code and Tenant Id : " + e);
		}
		return possibilityCheck;
	}

	@Override
	public List<String> getNewKpiIds(int numberOfIds) {
		String query = PerformanceAssessmentQueryBuilder.getNextKpiValueDetailId();
		Map<String, Object> paramValues = new HashMap<>();
		paramValues.put("size", numberOfIds);
		List<String> idList = new ArrayList<>();
		try {
			idList = namedParameterJdbcTemplate.queryForList(query, paramValues, String.class);
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

	@Override
	public List<ValueDocument> getDocsForValueRecords(List<String> valueIdList) {
		String query = PerformanceAssessmentQueryBuilder.getDocsForValueRecordQuery();
		final HashMap<String, Object> parametersMap = new HashMap<>();
		parametersMap.put("valueIdList", valueIdList);
		List<ValueDocument> documentList = namedParameterJdbcTemplate.query(query, parametersMap, new PerformanceAssessmentRowMapper().new ValueDocumentMapper());
		return documentList;
	}


}
