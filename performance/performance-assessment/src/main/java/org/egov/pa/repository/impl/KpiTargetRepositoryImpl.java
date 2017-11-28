package org.egov.pa.repository.impl;

import org.egov.pa.repository.KpiTargetRepository;
import org.egov.pa.web.contract.KPITargetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	

}
