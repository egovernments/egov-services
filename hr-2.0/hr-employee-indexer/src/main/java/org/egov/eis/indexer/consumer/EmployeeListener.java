package org.egov.eis.indexer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.eis.indexer.adaptor.EmployeeAdapter;
import org.egov.eis.indexer.config.PropertiesManager;
import org.egov.eis.indexer.model.EmployeeIndex;
import org.egov.eis.indexer.repository.ElasticSearchRepository;
import org.egov.eis.web.contract.EmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@NoArgsConstructor
public class EmployeeListener {

	private static final String INDEX_NAME = "employees";
	private static final String INDEX_TYPE = "employee";

	private ElasticSearchRepository elasticSearchRepository;
	private EmployeeAdapter employeeAdapter;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	public EmployeeListener(ElasticSearchRepository elasticSearchRepository, EmployeeAdapter employeeAdapter) {
		this.elasticSearchRepository = elasticSearchRepository;
		this.employeeAdapter = employeeAdapter;
	}

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = {"${kafka.topics.employee.esindex.savedb.name}",
			"${kafka.topics.employee.esindex.updatedb.name}" })
	public void listen(Map<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.info("topic : " + topic + " --&-- record : " + record + "\t\t");
		EmployeeRequest employeeRequest = objectMapper.convertValue(record, EmployeeRequest.class);
		if (employeeRequest != null) {
			EmployeeIndex newEmployeeIndex = employeeAdapter.indexOnCreate(employeeRequest);

			if (topic.equals(propertiesManager.getUpdateEmployeeIndexerTopic())) {
				EmployeeIndex esEmployeeIndex = elasticSearchRepository.getIndex(INDEX_NAME, INDEX_TYPE,
						employeeRequest.getEmployee().getId());
				if (!isEmpty(newEmployeeIndex))
					employeeAdapter.setOldCreatedByAndCreatedDateForUpdate(esEmployeeIndex, newEmployeeIndex);
			}

			elasticSearchRepository.index(INDEX_NAME, INDEX_TYPE, newEmployeeIndex.getEmployee().getId(), newEmployeeIndex);
		}
	}
}