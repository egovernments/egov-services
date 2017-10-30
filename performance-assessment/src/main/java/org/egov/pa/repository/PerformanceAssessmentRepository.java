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
package org.egov.pa.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.pa.model.Document;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiTarget;
import org.egov.pa.model.KpiTargetList;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.KpiValueList;
import org.egov.pa.repository.builder.PerformanceAssessmentQueryBuilder;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper.KPIMasterRowMapper;
import org.egov.pa.repository.rowmapper.PerformanceAssessmentRowMapper.KPIValueRowMapper;
import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIRequest;
import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PerformanceAssessmentRepository {

    public static final String roleCode = "CITIZEN"; 
    
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
    
    @Value("${kafka.topics.kpivalue.create.name}")
	private String createKpiValueTopic;
    
    @Value("${kafka.topics.kpivalue.update.name}")
	private String updateKpiValueTopic;
    
    public void persistKpi(final KPIRequest kpiRequest) {
    	log.info("Request before pushing to Kafka Queue : " + kpiRequest);
    	kafkaTemplate.send(saveNewKpiTopic, kpiRequest);
    }
    
    public void updateKpi(final KPIRequest kpiRequest) {
    	log.info("Request before pushing to Kafka Queue : " + kpiRequest);
    	kafkaTemplate.send(updateNewKpiTopic, kpiRequest); 
    }
    
    public void deleteKpi(final KPIRequest kpiRequest) {
    	log.info("Request before pushing to Kafka Queue : " + kpiRequest);
    	kafkaTemplate.send(deleteNewKpiTopic, kpiRequest); 
    }
    
    public void persistKpiTarget(final KpiTargetList kpiTargetList) { 
    	log.info("Request before pushing to Kafka Queue : " + kpiTargetList);
    	kafkaTemplate.send(saveKpiTargetTopic, kpiTargetList);
    }
    
    public void updateKpiTarget(final KpiTargetList kpiTargetList) { 
    	log.info("Request before pushing to Kafka Queue : " + kpiTargetList);
    	kafkaTemplate.send(updateKpiTargetTopic, kpiTargetList);
    }
    
    public void persistKpiValue(final KPIValueRequest kpiValueRequest) { 
    	log.info("Request before pushing to Kafka Queue : " + kpiValueRequest);
    	kafkaTemplate.send(createKpiValueTopic, kpiValueRequest);
    }
    
    public void updateKpiValue(final KPIValueRequest kpiValueRequest) { 
    	log.info("Request before pushing to Kafka Queue : " + kpiValueRequest);
    	kafkaTemplate.send(updateKpiValueTopic, kpiValueRequest);
    }
    
    public List<KpiValueList> searchKpiValue(KPIValueSearchRequest kpiValueSearchReq) {
    	final List<Object> preparedStatementValues = new ArrayList<>();
    	String query = queryBuilder.getValueSearchQuery(kpiValueSearchReq,preparedStatementValues);
    	KPIValueRowMapper mapper = new PerformanceAssessmentRowMapper().new KPIValueRowMapper(); 
    	List<KpiValueList> listOfValues = jdbcTemplate.query(query, preparedStatementValues.toArray(), mapper);
    	return listOfValues; 
    }
    
    public List<KPI> searchKpi(final KPIGetRequest kpiGetRequest) { 
    	final List<Object> preparedStatementValues = new ArrayList<>();
    	String query = queryBuilder.getKpiSearchQuery(kpiGetRequest, preparedStatementValues); 
    	KPIMasterRowMapper mapper = new PerformanceAssessmentRowMapper().new KPIMasterRowMapper(); 
    	jdbcTemplate.query(query, preparedStatementValues.toArray(), mapper);
    	List<KPI> kpiList = getListFromMapper(mapper.kpiMap);
    	if(null != kpiList && kpiList.size() > 0) { 
    		arrangeDocsToKpiList(kpiList, mapper.docMap);
    	}
    	return kpiList; 
    }
    
    private List<KPI> getListFromMapper(Map<Long, KPI> kpiMap) { 
    	Iterator<Entry<Long, KPI>> itr = kpiMap.entrySet().iterator();
    	List<KPI> kpiList = new ArrayList<>();
    	while(itr.hasNext()) { 
    		Entry<Long, KPI> entry = itr.next();
    		kpiList.add(entry.getValue()); 
    	}    
    	return kpiList;
    }
    
    private void arrangeDocsToKpiList(List<KPI> kpiList, Map<Long, List<Document>> map) { 
    	Iterator<Entry<Long, List<Document>>> itr = map.entrySet().iterator();
    	while(itr.hasNext()) { 
    		Entry<Long, List<Document>> entry = itr.next();
    		for(KPI kpi : kpiList) { 
    			if(kpi.getId() == entry.getKey()) { 
    				kpi.setDocuments(entry.getValue());
    			}
    		}
    	}
    }
    
	public List<Long> getIdList(int size, String sequenceName) {
		Map<String, Object> paramValues = new HashMap<>();
		paramValues.put("size", size);
		String getIdListQuery = "SELECT NEXTVAL('" + sequenceName + "') FROM GENERATE_SERIES(1,:size)";
		List<Long> idList;
		try {
			idList = namedParameterJdbcTemplate.queryForList(getIdListQuery, paramValues, Long.class);
		} catch (Exception e) {
			throw new RuntimeException("Next id is not generated.");
		}
		return idList;
	}
    
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
    
	public List<KPI> checkNameOrCodeExists(KPIRequest kpiRequest) {
		String query = PerformanceAssessmentQueryBuilder.fetchKpiByNameOrCode();
		List<KPI> kpiList = new ArrayList<>();
		for(int i=0 ; i < kpiRequest.getKpis().size() ; i ++) { 
			final HashMap<String, Object> parametersMap = new HashMap<>();
			parametersMap.put("name", kpiRequest.getKpis().get(i).getName());
			parametersMap.put("code", kpiRequest.getKpis().get(i).getCode());
			parametersMap.put("finyear", kpiRequest.getKpis().get(i).getFinYear());
			List<KPI> list = namedParameterJdbcTemplate.query(query,
	                parametersMap, new BeanPropertyRowMapper<>(KPI.class));
			kpiList.addAll(list); 
		}
		return kpiList;
	}
	
	public List<KPI> checkKpiExists(String kpiCode) {
		String query = PerformanceAssessmentQueryBuilder.fetchKpiByCode();
		final HashMap<String, Object> parametersMap = new HashMap<>();
		parametersMap.put("code", kpiCode);
		List<KPI> list = namedParameterJdbcTemplate.query(query, parametersMap, new BeanPropertyRowMapper<>(KPI.class));
		return list;
	}
	
	public List<KpiTarget> fetchTargetForKpi(String kpiCode, String finYear) { 
		String query = PerformanceAssessmentQueryBuilder.fetchTargetForKpi(); 
		final HashMap<String, Object> parametersMap = new HashMap<>(); 
		parametersMap.put("kpiCode", kpiCode); 
		parametersMap.put("finYear", finYear);
		List<KpiTarget> list = namedParameterJdbcTemplate.query(query, parametersMap, new BeanPropertyRowMapper<>(KpiTarget.class));
		return list;
	}
	
	public KpiValue checkKpiValueExistsForTenant(String kpiCode, String tenantId) { 
		String query = PerformanceAssessmentQueryBuilder.fetchKpiValueForCodeAndTenant(); 
		final HashMap<String, Object> parametersMap = new HashMap<>(); 
		parametersMap.put("kpiCode", kpiCode); 
		parametersMap.put("tenantId", tenantId);
		KpiValue value = null ; 
		try { 
			value = namedParameterJdbcTemplate.queryForObject(query, parametersMap, new BeanPropertyRowMapper<>(KpiValue.class));			
		} catch(EmptyResultDataAccessException exec) { 
			log.info("Empty Data Set resulted - Code and Tenant ID does not have a record : " + exec);
		} catch(Exception e) { 
			log.error("Encountered an exception while fetching the data for Code and Tenant Id : " + e);
		}
		return value;
	}
	
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

	
	
    
    
}
