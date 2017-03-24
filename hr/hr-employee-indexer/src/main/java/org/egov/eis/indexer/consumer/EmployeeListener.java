package org.egov.eis.indexer.consumer;


import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.egov.eis.indexer.adaptor.EmployeeAdapter;
import org.egov.eis.indexer.model.es.EmployeeIndex;
import org.egov.eis.indexer.repository.ElasticSearchRepository;
import org.egov.eis.web.contract.EmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class EmployeeListener {

	private static final String INDEX_NAME = "employees";
	private static final String INDEX_TYPE = "employee";

	private ElasticSearchRepository elasticSearchRepository;
	private EmployeeAdapter employeeAdapter;

	@Autowired
	public EmployeeListener(ElasticSearchRepository elasticSearchRepository, EmployeeAdapter employeeAdapter) {
		this.elasticSearchRepository = elasticSearchRepository;
		
		this.employeeAdapter = employeeAdapter;
	}

	@KafkaListener(containerFactory="kafkaListenerContainerFactory", topics = "${kafka.topics.egov.index.name}")
	public void listen(ConsumerRecord<String, String> record) {
		
		//System.err.println(record.value());
		ObjectMapper objectMapper=new ObjectMapper();
		EmployeeRequest employeeRequest = null;

		try {
			employeeRequest = objectMapper.readValue(record.value(), EmployeeRequest.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (employeeRequest != null) {
			EmployeeIndex employeeIndex = employeeAdapter.indexOnCreate(employeeRequest);
			/*ObjectMapper objectMapper2 = new ObjectMapper();
			try {
				System.err.println(objectMapper2.writeValueAsString(employeeIndex));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}*/

			elasticSearchRepository.index(INDEX_NAME, INDEX_TYPE, employeeIndex.getEmployeeDetails().getEmployeeId(),
					employeeIndex);
		}
	}
}