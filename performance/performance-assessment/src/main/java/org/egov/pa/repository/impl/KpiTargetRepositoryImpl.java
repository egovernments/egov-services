package org.egov.pa.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.egov.pa.model.KpiTarget;
import org.egov.pa.repository.KpiTargetRepository;
import org.egov.pa.repository.builder.PerformanceAssessmentQueryBuilder;
import org.egov.pa.web.contract.KPITargetGetRequest;
import org.egov.pa.web.contract.KPITargetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
    	String query = queryBuilder.getTargetSearchQuery();  
    	final HashMap<String, Object> parametersMap = new HashMap<>();
		parametersMap.put("kpiCode", getReq.getKpiCode());
		List<KpiTarget> list = namedParameterJdbcTemplate.query(query,
                parametersMap, new BeanPropertyRowMapper<>(KpiTarget.class));
		return list;
	}
	

}
