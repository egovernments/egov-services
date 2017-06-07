package org.egov.eis.indexer.consumer;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.eis.indexer.adaptor.EmployeeAdapter;
import org.egov.eis.indexer.config.PropertiesManager;
import org.egov.eis.indexer.model.es.EmployeeIndex;
import org.egov.eis.indexer.repository.ElasticSearchRepository;
import org.egov.eis.indexer.service.BoundaryService;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class EmployeeListener {

	private static final String INDEX_NAME = "employees";
	private static final String INDEX_TYPE = "employee";

	private ElasticSearchRepository elasticSearchRepository;
	private EmployeeAdapter employeeAdapter;

	public static final Logger LOGGER = LoggerFactory.getLogger(BoundaryService.class);

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	public EmployeeListener(ElasticSearchRepository elasticSearchRepository, EmployeeAdapter employeeAdapter) {
		this.elasticSearchRepository = elasticSearchRepository;
		this.employeeAdapter = employeeAdapter;
	}
	
	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = {
			"${kafka.topics.employee.esindex.savedb.name}", "${kafka.topics.employee.esindex.updatedb.name}" })
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("key : " + record.key() + "\t\t" + "value : " + record.value());

		EmployeeRequest employeeRequest = null;

		try {
			employeeRequest = objectMapper.readValue(record.value(), EmployeeRequest.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (employeeRequest != null) {
			EmployeeIndex newEmployeeIndex = employeeAdapter.indexOnCreate(employeeRequest);

			if (record.topic().equals(propertiesManager.getUpdateEmployeeIndexerTopic())) {
				EmployeeIndex esEmployeeIndex = elasticSearchRepository.getIndex(INDEX_NAME, INDEX_TYPE,
						employeeRequest.getEmployee().getId());
				employeeAdapter.setOldCreatedByAndCreatedDateForEditEmployee(esEmployeeIndex, newEmployeeIndex);
			}

			elasticSearchRepository.index(INDEX_NAME, INDEX_TYPE, newEmployeeIndex.getEmployeeDetails().getEmployeeId(),
					newEmployeeIndex);
		}
	}
}